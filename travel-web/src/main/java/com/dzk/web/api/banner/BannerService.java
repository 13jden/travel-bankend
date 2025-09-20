package com.dzk.web.api.banner;

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
            return banners;
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
