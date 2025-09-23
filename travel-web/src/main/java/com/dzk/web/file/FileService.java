package com.dzk.web.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService extends ServiceImpl<FileMapper, FileEntity> {

    @Autowired
    FileMapper fileMapper;

    @Value("${upload.commonPath}")
    private String uploadPath;
    
    /**
     * 上传文件
     */
    @Transactional
    public String upload(MultipartFile file, String path) {
        try {
            // 1. 检查文件是否为空
            if (file.isEmpty()) {
                throw new RuntimeException("文件不能为空");
            }
            
            // 2. 获取原始文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("文件名不能为空");
            }
            
            String extension = "";
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex > 0) {
                extension = originalFilename.substring(lastDotIndex);
            }
            
            // 3. 生成UUID作为文件名
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + extension;
            
            // 4. 创建上传目录
            String finalUploadPath = uploadPath;
            if (path != null && !path.isEmpty()) {
                finalUploadPath = uploadPath + path + "/";
            }
            
            File uploadDir = new File(finalUploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 5. 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            
            // 6. 保存文件信息到数据库
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUuid(uuid);
            fileEntity.setPath(finalUploadPath);
            fileEntity.setType(file.getContentType());
            fileEntity.setExtension(extension);
            fileEntity.setSize(file.getSize());
            this.save(fileEntity);

            

            // 7. 返回UUID
            return uuid;
            
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传文件（默认路径）
     */
    @Transactional
    public String upload(MultipartFile file) {
        return upload(file, null);
    }
    
    /**
     * 根据ID获取文件详情
     */
    @Transactional
    public FileDto getFileById(Long id) {
       FileEntity file = fileMapper.selectById(id);
       return FileDto.builder()
               .id(file.getId())
               .uuid(file.getUuid())
               .size(file.getSize())
               .url(file.getPath()+file.getUuid()+file.getExtension())
               .type(file.getType())
               .extension(file.getExtension())
               .build();
    }
    
    /**
     * 根据UUID获取文件实体
     */
    @Transactional
    public FileEntity getFileByUuid(String uuid) {
        QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        return fileMapper.selectOne(queryWrapper);
    }
    
    /**
     * 根据UUID下载文件
     */
    public ResponseEntity<Resource> downloadFile(String uuid) {
        try {
            // 1. 根据UUID获取文件信息
            FileEntity fileEntity = getFileByUuid(uuid);
            if (fileEntity == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 2. 构建文件路径
            String filePath = fileEntity.getPath() + fileEntity.getUuid() + fileEntity.getExtension();
            File file = new File(filePath);
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // 3. 创建文件资源
            Resource resource = new FileSystemResource(file);
            
            // 4. 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getUuid() + fileEntity.getExtension() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, fileEntity.getType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileEntity.getSize()));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(fileEntity.getType()))
                    .body(resource);
                    
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 根据UUID预览文件
     */
    public ResponseEntity<Resource> viewFile(String uuid) {
        try {
            // 1. 根据UUID获取文件信息
            FileEntity fileEntity = getFileByUuid(uuid);
            if (fileEntity == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 2. 构建文件路径
            String filePath = fileEntity.getPath() + fileEntity.getUuid() + fileEntity.getExtension();
            File file = new File(filePath);
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            // 3. 创建文件资源
            Resource resource = new FileSystemResource(file);
            
            // 4. 设置响应头（用于浏览器预览）
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, fileEntity.getType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileEntity.getSize()));
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(fileEntity.getType()))
                    .body(resource);
                    
        } catch (Exception e) {
            throw new RuntimeException("文件预览失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID删除文件
     */
    @Transactional
    public boolean deleteFileById(Long uuid) {
        try {
            // 1. 获取文件信息
            FileEntity file = fileMapper.selectById(uuid);
            if (file == null) {
                throw new RuntimeException("文件不存在");
            }
            
            // 2. 删除物理文件
            File physicalFile = new File(file.getPath());
            if (physicalFile.exists()) {
                physicalFile.delete();
            }
            
            // 3. 删除数据库记录
            QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", uuid);
            return this.remove(queryWrapper);
            
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败: " + e.getMessage());
        }
    }
}
