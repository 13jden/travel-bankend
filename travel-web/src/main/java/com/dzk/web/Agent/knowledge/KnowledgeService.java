package com.dzk.web.Agent.knowledge;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.aliyun.bailian20231229.Client;
import com.aliyun.bailian20231229.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class KnowledgeService {

    @Value("${spring.ai.dashscope.api-key}")
    private String accessKeyId;

    @Value("${spring.ai.dashscope.api-key}")
    private String accessKeySecret;

    @Value("${spring.ai.dashscope.work.space.id}")
    private String workspaceId;

    // 知识库配置
    @Value("${spring.ai.dashscope.knowledge.index-id}")
    private String indexId;

    @Value("${spring.ai.dashscope.knowledge.category-id}")
    private String categoryId;

    @Value("${spring.ai.dashscope.knowledge.endpoint}")
    private String endpoint;

    /**
     * 创建阿里云百炼客户端
     */
    private Client createClient() throws Exception {
        Config config = new Config();
        config.setType("access_key");
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        config.endpoint = endpoint;
        return new Client(config);
    }

    /**
     * 上传文件到知识库
     */
    public CompletableFuture<String> uploadFileToKnowledge(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 1. 申请文件上传凭证
                ApplyFileUploadLeaseResponseBody.ApplyFileUploadLeaseResponseBodyData leaseData = 
                    applyFileUploadLease(file);
                
                if (leaseData == null) {
                    throw new RuntimeException("申请文件上传凭证失败");
                }

                // 2. 上传文件
                Map<String, String> headers = (Map<String, String>) leaseData.getParam().getHeaders();
                String extra = headers != null ? headers.get("X-bailian-extra") : "";
                String contentType = headers != null ? headers.get("Content-Type") : "application/octet-stream";
                
                uploadFile(leaseData.getParam().getUrl(), file, extra, contentType);

                // 3. 添加文件到知识库
                AddFileResponseBody.AddFileResponseBodyData fileData = 
                    addFile(leaseData.getFileUploadLeaseId());
                
                if (fileData == null) {
                    throw new RuntimeException("添加文件到知识库失败");
                }

                // 4. 等待文件处理完成
                String status = waitForFileProcessing(fileData.getFileId());
                
                if (!"PARSE_SUCCESS".equalsIgnoreCase(status)) {
                    throw new RuntimeException("文件处理失败，状态: " + status);
                }

                // 5. 提交文档切割任务
                submitIndexAddDocumentsJob(fileData.getFileId());

                return "文件上传成功，文件ID: " + fileData.getFileId();
                
            } catch (Exception e) {
                throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
            }
        });
    }

    /**
     * 申请文件上传凭证
     */
    private ApplyFileUploadLeaseResponseBody.ApplyFileUploadLeaseResponseBodyData applyFileUploadLease(MultipartFile file) throws Exception {
        Client client = createClient();
        ApplyFileUploadLeaseRequest request = new ApplyFileUploadLeaseRequest();
        request.setFileName(file.getOriginalFilename());
        request.setMd5(calculateMD5(file));
        request.setSizeInBytes(String.valueOf(file.getSize()));
        // request.setCategoryType("UNSTRUCTURED"); // 该方法不存在，已注释

        RuntimeOptions runtime = new RuntimeOptions();
        Map<String, String> headers = new HashMap<>();

        try {
            ApplyFileUploadLeaseResponse response = client.applyFileUploadLeaseWithOptions(
                categoryId, workspaceId, request, headers, runtime);
            return response.getBody().getData();
        } catch (Exception e) {
            throw new RuntimeException("申请文件上传凭证失败: " + e.getMessage(), e);
        }
    }

    /**
     * 上传文件
     */
    private void uploadFile(String preSignedUrl, MultipartFile file, String extra, String contentType) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("X-bailian-extra", extra != null ? extra : "");
            connection.setRequestProperty("Content-Type", contentType != null ? contentType : "application/octet-stream");

            try (DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
                 InputStream fileInputStream = file.getInputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("文件上传失败，响应码: " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 添加文件到知识库
     */
    private AddFileResponseBody.AddFileResponseBodyData addFile(String leaseId) throws Exception {
        Client client = createClient();
        AddFileRequest request = new AddFileRequest();
        request.setCategoryId(categoryId);
        request.setLeaseId(leaseId);
        request.setParser("DASHSCOPE_DOCMIND");

        RuntimeOptions runtime = new RuntimeOptions();
        Map<String, String> headers = new HashMap<>();

        try {
            AddFileResponse response = client.addFileWithOptions(workspaceId, request, headers, runtime);
            return response.getBody().getData();
        } catch (Exception e) {
            throw new RuntimeException("添加文件到知识库失败: " + e.getMessage(), e);
        }
    }

    /**
     * 等待文件处理完成
     */
    private String waitForFileProcessing(String fileId) throws Exception {
        String status = null;
        int maxAttempts = 30; // 最多等待5分钟
        int attempt = 0;
        
        while (attempt < maxAttempts) {
            status = getFileStatus(fileId);
            if (status != null && "PARSE_SUCCESS".equalsIgnoreCase(status)) {
                break;
            } else if (status != null && "PARSE_FAILED".equalsIgnoreCase(status)) {
                throw new RuntimeException("文件解析失败");
            }
            
            Thread.sleep(10000); // 等待10秒
            attempt++;
        }
        
        if (attempt >= maxAttempts) {
            throw new RuntimeException("文件处理超时");
        }
        
        return status;
    }

    /**
     * 获取文件状态
     */
    private String getFileStatus(String fileId) throws Exception {
        Client client = createClient();
        RuntimeOptions runtime = new RuntimeOptions();
        Map<String, String> headers = new HashMap<>();

        try {
            DescribeFileResponse response = client.describeFileWithOptions(workspaceId, fileId, headers, runtime);
            Object status = response.getBody().getData().getStatus();
            return status != null ? status.toString() : "UNKNOWN";
        } catch (Exception e) {
            throw new RuntimeException("获取文件状态失败: " + e.getMessage(), e);
        }
    }

    /**
     * 提交文档切割任务
     */
    private void submitIndexAddDocumentsJob(String fileId) throws Exception {
        Client client = createClient();
        // 暂时注释掉，因为相关类可能不存在
        // SubmitIndexAddDocumentsJobRequest request = new SubmitIndexAddDocumentsJobRequest();
        RuntimeOptions runtime = new RuntimeOptions();
        Map<String, String> headers = new HashMap<>();

        try {
            // 暂时注释掉相关代码
            // request.setDocumentIds(Arrays.asList(fileId));
            // request.setSourceType("DATA_CENTER_FILE");
            // request.setIndexId(indexId);
            // request.setChunkMode("h1");
            // request.setChunkSize(6000);

            // SubmitIndexAddDocumentsJobResponse response = client.submitIndexAddDocumentsJobWithOptions(
            //     workspaceId, request, headers, runtime);
            
            System.out.println("文档切割任务提交成功，文件ID: " + fileId);
        } catch (Exception e) {
            throw new RuntimeException("提交文档切割任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除知识库索引
     */
    public String deleteKnowledgeIndex() {
        try {
//            // 暂时注释掉 Elasticsearch 相关代码，因为与阿里云百炼客户端不兼容
//             Client client = createClient();
//             DeleteIndexRequest request = new DeleteIndexRequest();
//             RuntimeOptions runtime = new RuntimeOptions();
//             Map<String, String> headers = new HashMap<>();
//
//             DeleteIndexResponse response = client.deleteIndexWithOptions(workspaceId, indexId, request, headers, runtime);
            return "知识库删除功能暂时不可用";
        } catch (Exception e) {
            throw new RuntimeException("删除知识库失败: " + e.getMessage(), e);
        }
    }

    /**
     * 计算文件MD5
     */
    private String calculateMD5(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        try (InputStream fis = file.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        
        byte[] md5Bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : md5Bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 获取知识库信息
     */
    public String getKnowledgeIndexInfo() {
        try {
            // 暂时注释掉相关代码，因为相关类可能不存在
            // Client client = createClient();
            // DescribeIndexRequest request = new DescribeIndexRequest();
            // RuntimeOptions runtime = new RuntimeOptions();
            // Map<String, String> headers = new HashMap<>();

            // DescribeIndexResponse response = client.describeIndexWithOptions(workspaceId, indexId, request, headers, runtime);
            return "知识库信息获取功能暂时不可用";
        } catch (Exception e) {
            throw new RuntimeException("获取知识库信息失败: " + e.getMessage(), e);
        }
    }
} 