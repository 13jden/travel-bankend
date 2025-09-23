package com.dzk.web.api.guide;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.web.file.FileEntity;
import com.dzk.web.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class GuideService extends ServiceImpl<GuideMapper, Guide> {

    @Autowired
    GuideMapper guideMapper;
    
    @Autowired
    FileService fileService;
    
    @Value("${upload.commonPath}")
    private String uploadPath;

    /**
     * 搜索攻略
     */
    public List<GuideDto> search(GuideCriteria criteria, Pageable pageable) {
        QueryWrapper<Guide> queryWrapper = new QueryWrapper<>();
        
        if (criteria.getTitle() != null && !criteria.getTitle().isEmpty()) {
            queryWrapper.like("title", criteria.getTitle());
        }
        if (criteria.getTitleEn() != null && !criteria.getTitleEn().isEmpty()) {
            queryWrapper.like("title_en", criteria.getTitleEn());
        }
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            queryWrapper.eq("type", criteria.getType());
        }
        if (criteria.getIsEnable() != null) {
            queryWrapper.eq("is_enable", criteria.getIsEnable());
        }
        if (criteria.getIsRecommend() != null) {
            queryWrapper.eq("is_recommend", criteria.getIsRecommend());
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<Guide> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<Guide> guidePage = this.page(page, queryWrapper);
        
        List<GuideDto> dtos = GuideConverter.toDtoList(guidePage.getRecords());
        
        // 设置封面图片URL、markdown路径和内容
        for (GuideDto dto : dtos) {
            if (dto.getCoverImageId() != null) {
                FileEntity coverImage = fileService.getById(dto.getCoverImageId());
                if (coverImage != null) {
                    dto.setCoverImageUrl(buildImageUrl(coverImage));
                }
            }
            if (dto.getMarkdownUuid() != null) {
                dto.setMarkdownPath(buildMarkdownPath(dto.getMarkdownUuid()));
                // 从markdown文件读取内容
                dto.setContent(getMarkdownContentFromFile(dto.getMarkdownUuid()));
            }
        }
        
        return dtos;
    }

    /**
     * 添加攻略
     */
    @Transactional
    public GuideDto add(GuideDto.Input input) {
        try {
            // 1. 验证封面图片是否存在
            FileEntity coverImage = fileService.getById(input.getCoverImageId());
            if (coverImage == null) {
                throw new BusinessException("封面图片不存在");
            }
            
            // 2. 创建攻略实体
            Guide guide = GuideConverter.toEntity(input);
            
            // 3. 处理markdown内容，生成markdown文件
            String markdownUuid = generateMarkdownFile(input.getContent(), input.getTitle());
            guide.setMarkdownUuid(markdownUuid);
            
            // 4. 保存攻略
            guideMapper.insert(guide);
            
            // 5. 返回DTO
            GuideDto dto = GuideConverter.toDto(guide);
            dto.setCoverImageUrl(buildImageUrl(coverImage));
            dto.setMarkdownPath(buildMarkdownPath(markdownUuid));
            // 从markdown文件读取内容
            dto.setContent(getMarkdownContentFromFile(markdownUuid));
            
            return dto;
            
        } catch (Exception e) {
            throw new BusinessException("添加攻略失败: " + e.getMessage());
        }
    }

    /**
     * 更新攻略
     */
    @Transactional
    public GuideDto update(Long id, GuideDto.Input input) {
        try {
            Guide existingGuide = guideMapper.selectById(id);
            if (existingGuide == null) {
                throw new BusinessException("攻略不存在！");
            }
            
            // 验证封面图片是否存在
            FileEntity coverImage = fileService.getById(input.getCoverImageId());
            if (coverImage == null) {
                throw new BusinessException("封面图片不存在");
            }
            
            // 更新攻略信息
            Guide guide = GuideConverter.toEntity(input);
            guide.setId(id);
            guide.setClickCount(existingGuide.getClickCount());
            guide.setLikeCount(existingGuide.getLikeCount());
            guide.setCollectCount(existingGuide.getCollectCount());
            
            // 处理markdown内容
            String markdownUuid = generateMarkdownFile(input.getContent(), input.getTitle());
            guide.setMarkdownUuid(markdownUuid);
            
            // 删除旧的markdown文件
            if (existingGuide.getMarkdownUuid() != null) {
                deleteMarkdownFile(existingGuide.getMarkdownUuid());
            }
            
            guideMapper.updateById(guide);
            
            GuideDto dto = GuideConverter.toDto(guide);
            dto.setCoverImageUrl(buildImageUrl(coverImage));
            dto.setMarkdownPath(buildMarkdownPath(markdownUuid));
            // 从markdown文件读取内容
            dto.setContent(getMarkdownContentFromFile(markdownUuid));
            
            return dto;
            
        } catch (Exception e) {
            throw new BusinessException("更新攻略失败: " + e.getMessage());
        }
    }

    /**
     * 获取攻略详情
     */
    public GuideDto getDetail(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide == null) {
            throw new BusinessException("攻略不存在");
        }
        
        GuideDto dto = GuideConverter.toDto(guide);
        
        // 设置封面图片URL
        if (guide.getCoverImageId() != null) {
            FileEntity coverImage = fileService.getById(guide.getCoverImageId());
            if (coverImage != null) {
                dto.setCoverImageUrl(buildImageUrl(coverImage));
            }
        }
        
        // 设置markdown路径和内容
        if (guide.getMarkdownUuid() != null) {
            dto.setMarkdownPath(buildMarkdownPath(guide.getMarkdownUuid()));
            // 从markdown文件读取内容
            dto.setContent(getMarkdownContentFromFile(guide.getMarkdownUuid()));
        }
        
        return dto;
    }

    /**
     * 删除攻略
     */
    @Transactional
    public GuideDto delete(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide == null) {
            throw new BusinessException("攻略不存在");
        }
        
        try {
            // 删除markdown文件
            if (guide.getMarkdownUuid() != null) {
                deleteMarkdownFile(guide.getMarkdownUuid());
            }
            
            guideMapper.deleteById(id);
            
            return GuideConverter.toDto(guide);
        } catch (Exception e) {
            throw new BusinessException("删除攻略失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除攻略
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        try {
            for (Long id : ids) {
                Guide guide = guideMapper.selectById(id);
                if (guide != null && guide.getMarkdownUuid() != null) {
                    deleteMarkdownFile(guide.getMarkdownUuid());
                }
            }
            guideMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            throw new BusinessException("批量删除攻略失败: " + e.getMessage());
        }
    }

    /**
     * 增加点击量
     */
    @Transactional
    public void incrementClickCount(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide != null) {
            guide.setClickCount(guide.getClickCount() + 1);
            guideMapper.updateById(guide);
        }
    }

    /**
     * 增加点赞量
     */
    @Transactional
    public void incrementLikeCount(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide != null) {
            guide.setLikeCount(guide.getLikeCount() + 1);
            guideMapper.updateById(guide);
        }
    }

    /**
     * 增加收藏量
     */
    @Transactional
    public void incrementCollectCount(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide != null) {
            guide.setCollectCount(guide.getCollectCount() + 1);
            guideMapper.updateById(guide);
        }
    }

    /**
     * 生成markdown文件
     */
    private String generateMarkdownFile(String content, String title) {
        try {
            // 创建markdown目录
            String markdownDir = uploadPath + "markdown/";
            File dir = new File(markdownDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 生成UUID和文件名
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + ".md";
            String filePath = markdownDir + fileName;
            
            // 写入文件
            try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(content);
            }
            
            return uuid;
            
        } catch (IOException e) {
            throw new BusinessException("生成markdown文件失败: " + e.getMessage());
        }
    }

    /**
     * 删除markdown文件
     */
    private void deleteMarkdownFile(String uuid) {
        try {
            String filePath = uploadPath + "markdown/" + uuid + ".md";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // 删除失败不影响主流程
            System.err.println("删除markdown文件失败: " + e.getMessage());
        }
    }

    /**
     * 构建图片URL
     */
    private String buildImageUrl(FileEntity fileEntity) {
        return fileEntity.getPath() + fileEntity.getUuid() + fileEntity.getExtension();
    }

    /**
     * 构建markdown文件路径
     */
    private String buildMarkdownPath(String uuid) {
        return uploadPath + "markdown/" + uuid + ".md";
    }

    /**
     * 从markdown文件读取内容
     */
    private String getMarkdownContentFromFile(String uuid) {
        try {
            String filePath = buildMarkdownPath(uuid);
            File file = new File(filePath);
            if (file.exists()) {
                return new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            } else {
                return ""; // 文件不存在时返回空字符串
            }
        } catch (IOException e) {
            return ""; // 读取失败时返回空字符串
        }
    }

    /**
     * 获取markdown文件内容（公开方法）
     */
    public String getMarkdownContent(Long id) {
        Guide guide = guideMapper.selectById(id);
        if (guide == null) {
            throw new BusinessException("攻略不存在");
        }
        
        if (guide.getMarkdownUuid() == null) {
            return ""; // 如果没有markdown文件，返回空字符串
        }
        
        return getMarkdownContentFromFile(guide.getMarkdownUuid());
    }
} 