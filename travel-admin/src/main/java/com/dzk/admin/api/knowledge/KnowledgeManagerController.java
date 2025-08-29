package com.dzk.admin.api.knowledge;

import com.dzk.web.Agent.knowledge.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/knowledge")
@CrossOrigin(origins = "*")
public class KnowledgeManagerController {

    @Autowired
    private KnowledgeService knowledgeService;

    /**
     * 上传文件到知识库
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要上传的文件");
                return ResponseEntity.badRequest().body(response);
            }

            // 异步处理文件上传
            CompletableFuture<String> future = knowledgeService.uploadFileToKnowledge(file);

            response.put("success", true);
            response.put("message", "文件上传任务已提交，正在处理中...");
            response.put("taskId", System.currentTimeMillis()); // 简单的任务ID

            // 异步获取结果
            future.thenAccept(result -> {
                System.out.println("文件上传完成: " + result);
            }).exceptionally(throwable -> {
                System.err.println("文件上传失败: " + throwable.getMessage());
                return null;
            });

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 删除知识库索引
     */
    @DeleteMapping("/index")
    public ResponseEntity<Map<String, Object>> deleteKnowledgeIndex() {
        Map<String, Object> response = new HashMap<>();

        try {
            String result = knowledgeService.deleteKnowledgeIndex();
            response.put("success", true);
            response.put("message", result);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除知识库失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取知识库信息
     */
    @GetMapping("/index/info")
    public ResponseEntity<Map<String, Object>> getKnowledgeIndexInfo() {
        Map<String, Object> response = new HashMap<>();

        try {
            String info = knowledgeService.getKnowledgeIndexInfo();
            response.put("success", true);
            response.put("data", info);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取知识库信息失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 检查知识库状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getKnowledgeStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            response.put("success", true);
            response.put("data", Map.of(
                    "indexId", "wp4oycjp11",
                    "categoryId", "cate_0cbcf2fd7d0844b79e0ea38dff1940a5_10114795",
                    "workspaceId", "llm-ycvym6ek3uwuisz0",
                    "status", "active"
            ));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取状态失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 批量上传文件
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<Map<String, Object>> uploadBatchFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (files.length == 0) {
                response.put("success", false);
                response.put("message", "请选择要上传的文件");
                return ResponseEntity.badRequest().body(response);
            }

            int successCount = 0;
            int failCount = 0;

            for (MultipartFile file : files) {
                try {
                    CompletableFuture<String> future = knowledgeService.uploadFileToKnowledge(file);
                    future.thenAccept(result -> {
                        System.out.println("文件 " + file.getOriginalFilename() + " 上传完成: " + result);
                    }).exceptionally(throwable -> {
                        System.err.println("文件 " + file.getOriginalFilename() + " 上传失败: " + throwable.getMessage());
                        return null;
                    });
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    System.err.println("文件 " + file.getOriginalFilename() + " 上传失败: " + e.getMessage());
                }
            }

            response.put("success", true);
            response.put("message", String.format("批量上传任务已提交，成功: %d, 失败: %d", successCount, failCount));
            response.put("totalFiles", files.length);
            response.put("successCount", successCount);
            response.put("failCount", failCount);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量上传失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}