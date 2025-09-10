package com.dzk.admin.api.banner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dzk.common.exception.BusinessException;
import com.dzk.common.redis.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class BannerService{

        @Autowired
        private BannerMapper bannerMapper;

        @Autowired
        private RedisComponent redisComponent;

        @Value("${upload.tempPath}")
        private String tempPath;

        @Value("${upload.bannerFilePath}")
        private String filePath;

        @Value("${host.url}")
        private String hostUrl;

        /**
         * 获取Banner列表，按sort排序
         * @return Banner列表
         */
        public List<Banner> getList() {

            // 缓存中没有，从数据库获取
            QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort");
            List<Banner> banners = bannerMapper.selectList();
            // 转换图片URL
            convertImageUrls(banners);
            return banners;
        }

        /**
         * 转换图片URL为完整访问路径
         * @param banners Banner列表
         */
        private void convertImageUrls(List<Banner> banners) {
            if (banners != null && !banners.isEmpty()) {
                for (Banner banner : banners) {
                    if (banner.getImage() != null && !banner.getImage().isEmpty()) {
                        String imageUrl = banner.getImage();
                        // 检查URL是否已经包含基础路径
                        if (!imageUrl.startsWith("http://")) {
                            // 使用正确的映射路径
                            String baseUrl = hostUrl + "/images/banner/";
                            banner.setImage(baseUrl + imageUrl);
                        }
                    }
                }
            }
        }

        /**
         * 添加Banner
         * @param image 图片
         * @param type 类型：1为场地，2为商品，3为笔记
         * @param isActive 是否启用：1是，0否
         * @param text 标题（可选）
         * @param contentId 跳转ID（可选）
         * @return 是否添加成功
         */

        public boolean add(String image, Integer type, Integer isActive, String text, Integer contentId) {
            // 校验参数
            if (image == null || image.trim().isEmpty()) {
                throw new BusinessException("图片不能为空");
            }

            // 获取文件名（去除路径信息）
            String fileName = new File(image).getName();

            // 规范化临时文件路径
//        String normalizedTempPath = tempPath.replace('\\', '/').replace("//", "/");
//        String normalizedFileName = fileName.replace('\\', '/');
            this.confirmImage(fileName);

            Banner banner = new Banner();
            banner.setImage(fileName);
            banner.setType(type);
            banner.setIsActive(isActive);
            banner.setText(text); // 可选
            banner.setContentId(contentId); // 可选

            // 获取最大排序值并加1
            Integer maxSort = bannerMapper.getMaxSort();
            banner.setSort(maxSort == null ? 1 : maxSort + 1);

            int result = bannerMapper.insert(banner);
            if (result<=0) {
                throw new BusinessException("添加Banner失败");
            }

            // 删除缓存
            redisComponent.deleteBannerList();
            return true;
        }

        /**
         * 更新Banner
         * @param bannerId Banner ID
         * @param image 图片（可选）
         * @param type 类型：1为场地，2为商品，3为笔记（可选）
         * @param isActive 是否启用：1是，0否（可选）
         * @param text 标题（可选）
         * @param contentId 跳转ID（可选）
         * @return 是否更新成功
         */

        public boolean update(Integer bannerId, String image, Integer type, Integer isActive, String text, Integer contentId) {
            // bannerId 是必需的
            if (bannerId == null) {
                throw new BusinessException("Banner ID不能为空");
            }
            Banner banner = bannerMapper.selectById(bannerId);
            if (banner == null) {
                throw new BusinessException("Banner不存在");
            }

            boolean needUpdate = false;

            // 只有在参数不为 null 时才更新对应字段
            if (image != null) {
                if (image.trim().isEmpty()) {
                    throw new BusinessException("图片不能为空字符串");
                }
                String fileName = new File(image).getName();
                this.confirmImage(fileName);
                banner.setImage(fileName);
                needUpdate = true;
            }

            if (type != null) {

                banner.setType(type);
                needUpdate = true;
            }

            if (isActive != null) {
                if (isActive != 0 && isActive != 1) {
                    throw new BusinessException("启用状态只能是0或1");
                }
                banner.setIsActive(isActive);
                needUpdate = true;
            }

            // text 和 contentId 允许设置为 null
            if (text != null || text == null && banner.getText() != null) {
                banner.setText(text); // 可以设置为 null
                needUpdate = true;
            }

            if (contentId != null || contentId == null && banner.getContentId() != null) {
                banner.setContentId(contentId); // 可以设置为 null
                needUpdate = true;
            }

            // 只有在有字段更新时才执行更新操作
            if (needUpdate) {
                int result = bannerMapper.updateById(banner);
                if (result<=0) {
                    throw new BusinessException("更新Banner失败");
                }
                // 删除缓存
                redisComponent.deleteBannerList();
            }

            return true;
        }

        /**
         * 删除Banner
         * @param bannerId Banner ID
         * @return 是否删除成功
         */

        @Transactional(rollbackFor = Exception.class)
        public boolean removeById(Integer bannerId) {
            if (bannerId == null) {
                throw new BusinessException("Banner ID不能为空");
            }

            // 检查Banner是否存在
            Banner banner = bannerMapper.selectById(bannerId);
            if (banner == null) {
                throw new BusinessException("Banner不存在");
            }

            // 获取被删除Banner的排序值
            Integer deletedSort = Math.toIntExact(banner.getSort());

            // 获取最大排序值
            Integer maxSort = bannerMapper.getMaxSort();

            // 删除Banner
            int result = bannerMapper.deleteById(bannerId);
            if (result<=0) {
                throw new BusinessException("删除Banner失败");
            }

            // 如果删除的不是最后一条数据，才需要更新其他Banner的排序值
            if (deletedSort < maxSort) {
                boolean updateResult = bannerMapper.updateSortAfterDelete(deletedSort);
                if (!updateResult) {
                    throw new BusinessException("更新其他Banner排序失败");
                }
            }

            // 删除对应的图片文件
            this.deleteFile(banner.getImage());

            // 删除缓存
            redisComponent.deleteBannerList();
            return true;
        }

        /**
         * 更新Banner排序
         * @param sortList 排序列表，包含bannerId和sort
         * @return 是否更新成功
         */
        @Transactional(rollbackFor = Exception.class)
        public boolean updateSort(List<Map<String, Integer>> sortList) {
            if (sortList == null || sortList.isEmpty()) {
                throw new BusinessException("排序列表不能为空");
            }

            for (Map<String, Integer> sortItem : sortList) {
                Integer bannerId = sortItem.get("bannerId");
                Integer sort = sortItem.get("sort");

                if (bannerId == null || sort == null) {
                    throw new BusinessException("排序项缺少必要参数");
                }

                // 检查Banner是否存在
                if (bannerMapper.selectById(bannerId) == null) {
                    throw new BusinessException("ID为" + bannerId + "的Banner不存在");
                }

                // 使用mapper直接更新排序值
                int result = bannerMapper.updateSort(bannerId, sort);
                if (result <= 0) {
                    throw new BusinessException("更新ID为" + bannerId + "的Banner排序失败");
                }
            }

            // 删除缓存
            redisComponent.deleteBannerList();
            return true;
        }

        /**
         * 确认图片，移动到正式目录
         * @param fileName 文件名
         * @return 确认结果
         */
        public boolean confirmImage(String fileName) {

            // 规范化临时文件路径
//        String normalizedTempPath = tempPath.replace('\\', '/').replace("//", "/");
//        String normalizedFileName = fileName.replace('\\', '/');


            // 使用 Paths.get 来正确处理路径
            File tempFile = Paths.get(tempPath, fileName).toFile();

            if (!tempFile.exists()) {
                throw new BusinessException("临时文件不存在: " + tempFile.getAbsolutePath());
            }

            try {
                // 使用 Paths.get 来构建目标路径
                Files.move(tempFile.toPath(), Paths.get(filePath, fileName));
                // 更新 image 为只包含文件名，存入数据库
            } catch (IOException e) {
                throw new BusinessException("移动图片失败: " + e.getMessage());
            }
            return true;
        }

        /**
         * 删除正式文件
         * @param fileName 文件名
         * @return 删除结果
         */
        public boolean deleteFile(String fileName) {
            File file = new File(filePath, fileName);
            if (file.exists() && !file.delete()) {
                throw new BusinessException("文件删除失败");
            }
            return true;
        }
}
