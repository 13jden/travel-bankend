package com.dzk.web.file;

import com.dzk.common.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
@Tag(name = "文件管理")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到服务器，返回UUID")
    public Result<Long> upload(@RequestPart("file") MultipartFile file) {
        return Result.success(fileService.upload(file));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件详情", description = "根据ID获取文件详情")
    public Result<FileDto> getFileById(@PathVariable Long id) {
        return Result.success(fileService.getFileById(id));
    }

    @GetMapping("/download/{uuid}")
    @Operation(summary = "下载文件", description = "根据UUID直接下载文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid) {
        return fileService.downloadFile(uuid);
    }

    @GetMapping("/view/{uuid}")
    @Operation(summary = "预览文件", description = "根据UUID预览文件（在浏览器中直接显示）")
    public ResponseEntity<Resource> viewFile(@PathVariable String uuid) {
        return fileService.viewFile(uuid);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "根据ID删除文件")
    public Result deleteFileById(@PathVariable Long id) {
        return Result.success(fileService.deleteFileById(id));
    }
}       