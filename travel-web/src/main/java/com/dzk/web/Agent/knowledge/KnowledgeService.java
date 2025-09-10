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
import java.util.Collections;
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
     * 申请文档上传租约。
     *
     * @param client      客户端对象
     * @param categoryId  类目ID
     * @param fileName    文档名称
     * @param fileMd5     文档的MD5值
     * @param fileSize    文档大小（以字节为单位）
     * @param workspaceId 业务空间ID
     * @return 阿里云百炼服务的响应对象
     */
    public ApplyFileUploadLeaseResponse applyLease(com.aliyun.bailian20231229.Client client, String categoryId, String fileName, String fileMd5, String fileSize, String workspaceId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        com.aliyun.bailian20231229.models.ApplyFileUploadLeaseRequest applyFileUploadLeaseRequest = new com.aliyun.bailian20231229.models.ApplyFileUploadLeaseRequest();
        applyFileUploadLeaseRequest.setFileName(fileName);
        applyFileUploadLeaseRequest.setMd5(fileMd5);
        applyFileUploadLeaseRequest.setSizeInBytes(fileSize);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        ApplyFileUploadLeaseResponse applyFileUploadLeaseResponse = null;
        applyFileUploadLeaseResponse = client.applyFileUploadLeaseWithOptions(categoryId, workspaceId, applyFileUploadLeaseRequest, headers, runtime);
        return applyFileUploadLeaseResponse;
    }

    /**
     * 上传到临时存储
     * @param preSignedUrl  上面接口实际返回的Data.Param.Headers中X-bailian-extra字段的值
     * @param filePath   实际返回的Data.Param.Headers中Content-Type字段的值（返回空值时，传空值即可）
     */
    public static void uploadFile(String preSignedUrl, String filePath) {
        HttpURLConnection connection = null;
        try {
            // 创建URL对象
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法用于文档上传，需与您在上一步中调用ApplyFileUploadLease接口实际返回的Data.Param中Method字段的值一致
            connection.setRequestMethod("PUT");
            // 允许向connection输出，因为这个连接是用于上传文档的
            connection.setDoOutput(true);
            connection.setRequestProperty("X-bailian-extra", "请替换为您在上一步中调用ApplyFileUploadLease接口实际返回的Data.Param.Headers中X-bailian-extra字段的值");
            connection.setRequestProperty("Content-Type", "请替换为您在上一步中调用ApplyFileUploadLease接口实际返回的Data.Param.Headers中Content-Type字段的值（返回空值时，传空值即可）");
            // 读取文档并通过连接上传
            try (DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
                 FileInputStream fileInputStream = new FileInputStream(filePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                outStream.flush();
            }
            // 检查响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 文档上传成功处理
                System.out.println("File uploaded successfully.");
            } else {
                // 文档上传失败处理
                System.out.println("Failed to upload the file. ResponseCode: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 将文档添加到类目中。
     *
     * @param client      客户端对象
     * @param leaseId     租约ID
     * @param parser      用于文档的解析器
     * @param categoryId  类目ID
     * @param workspaceId 业务空间ID
     * @return 阿里云百炼服务的响应对象
     */
    public AddFileResponse addFile(com.aliyun.bailian20231229.Client client, String leaseId, String parser, String categoryId, String workspaceId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        com.aliyun.bailian20231229.models.AddFileRequest addFileRequest = new com.aliyun.bailian20231229.models.AddFileRequest();
        addFileRequest.setLeaseId(leaseId);
        addFileRequest.setParser(parser);
        addFileRequest.setCategoryId(categoryId);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        return client.addFileWithOptions(workspaceId, addFileRequest, headers, runtime);
    }

    /**
     * 查询文档的基本信息。
     *
     * @param client      客户端对象
     * @param workspaceId 业务空间ID
     * @param fileId      文档ID
     * @return 阿里云百炼服务的响应对象
     */
    public DescribeFileResponse describeFile(com.aliyun.bailian20231229.Client client, String workspaceId, String fileId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        return client.describeFileWithOptions(workspaceId, fileId, headers, runtime);
    }

    /**
     * 向阿里云百炼服务提交索引任务。
     *
     * @param client      客户端对象
     * @param workspaceId 业务空间ID
     * @param indexId     知识库ID
     * @return 阿里云百炼服务的响应对象
     */
    public SubmitIndexJobResponse submitIndex(com.aliyun.bailian20231229.Client client, String workspaceId, String indexId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        com.aliyun.bailian20231229.models.SubmitIndexJobRequest submitIndexJobRequest = new com.aliyun.bailian20231229.models.SubmitIndexJobRequest();
        submitIndexJobRequest.setIndexId(indexId);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        return client.submitIndexJobWithOptions(workspaceId, submitIndexJobRequest, headers, runtime);
    }

    /**
     * 查询索引任务状态。
     *
     * @param client      客户端对象
     * @param workspaceId 业务空间ID
     * @param jobId       任务ID
     * @param indexId     知识库ID
     * @return 阿里云百炼服务的响应对象
     */
    public GetIndexJobStatusResponse getIndexJobStatus(com.aliyun.bailian20231229.Client client, String workspaceId, String jobId, String indexId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        com.aliyun.bailian20231229.models.GetIndexJobStatusRequest getIndexJobStatusRequest = new com.aliyun.bailian20231229.models.GetIndexJobStatusRequest();
        getIndexJobStatusRequest.setIndexId(indexId);
        getIndexJobStatusRequest.setJobId(jobId);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        GetIndexJobStatusResponse getIndexJobStatusResponse = null;
        getIndexJobStatusResponse = client.getIndexJobStatusWithOptions(workspaceId, getIndexJobStatusRequest, headers, runtime);
        return getIndexJobStatusResponse;
    }

    /**
     * 从指定的非结构化知识库中永久删除一个或多个文档
     *
     * @param client      客户端（Client）
     * @param workspaceId 业务空间ID
     * @param indexId     知识库ID
     * @param fileId      文档ID
     * @return 阿里云百炼服务的响应
     */
    public DeleteIndexDocumentResponse deleteIndexDocument(com.aliyun.bailian20231229.Client client, String workspaceId, String indexId, String fileId) throws Exception {
        Map<String, String> headers = new HashMap<>();
        DeleteIndexDocumentRequest deleteIndexDocumentRequest = new DeleteIndexDocumentRequest();
        deleteIndexDocumentRequest.setIndexId(indexId);
        deleteIndexDocumentRequest.setDocumentIds(Collections.singletonList(fileId));
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        return client.deleteIndexDocumentWithOptions(workspaceId, deleteIndexDocumentRequest, headers, runtime);
    }

} 