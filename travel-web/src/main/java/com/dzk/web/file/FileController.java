package com.dzk.web.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("file")
@Tag(name = "FileController", description = "文件管理")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到服务器，返回UUID")
    public String upload(@RequestParam("file") MultipartFile file) {
        return fileService.upload(file);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件详情", description = "根据UUID获取文件详情")
    public FileDto getFileByUuid(@PathVariable Long id) {
        return fileService.getFileById(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "根据UUID删除文件")
    public boolean deleteFileByUuid(@PathVariable Long id) {
        return fileService.deleteFileById(id);
    }
}       