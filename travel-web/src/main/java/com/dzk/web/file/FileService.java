package com.dzk.web.file;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService extends ServiceImpl<FileMapper, FileEntity> {

    @Autowired
    FileMapper fileMapper;

    @Value("${file.upload.path:/opt/travel/files}")
    private String uploadPath;
    
    /**
     * 上传文件
     */
    public String upload(MultipartFile file) {
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
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 5. 保存文件
            File destFile = new File(uploadDir, fileName);
            file.transferTo(destFile);
            
            // 6. 保存文件信息到数据库
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUuid(uuid);
            fileEntity.setPath(uploadPath);
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
     * 根据UUID获取文件详情
     */
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
     * 根据UUID删除文件
     */
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
