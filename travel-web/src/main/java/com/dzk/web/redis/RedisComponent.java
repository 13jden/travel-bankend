package com.dzk.common.redis;

import com.dzk.common.constants.Constants;


import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code){
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey,code, Constants.REDIS_KEY_EXPIRES_TIME);
        return checkCodeKey;
    }

    public void saveCheckCode(String checkCodeKey, String code){
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey,code, Constants.REDIS_KEY_EXPIRES_TIME);
    }

    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void setAdminToken(String token) {
        this.redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN + token,"admin",Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }
    public String getAdminToken(String token){
        return  (String)redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN  + token);
    }




    public String saveAdminTokenInfo(String account){
        String token = UUID.randomUUID().toString();
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN+token,account,-1);
        return token;
    }


    public void cleanToken(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }
    public void cleanCheckCode(String checkCodeKey){
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
    }
    public void cleanAdminToken(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN+token);
    }


    /**
     * 删除分类列表缓存
     */
    public void deleteCategoryList() {
        redisUtils.delete(Constants.REDIS_KEY_CATEGORY_LIST);
    }



    public void deleteBannerList() {
        redisUtils.delete(Constants.REDIS_KEY_BANNER_LIST);
    }


    public void saveNoTokenInfo(String pre,String userId) {
        redisUtils.set(pre+userId,Constants.REDIS_KEY_EXPIRES_ONE_DAY);
    }

    public boolean getNoTokenInfo(String key) {
        return  (redisUtils.get(key)!=null);
    }

    public void deleteNoTokenInfo(String invalidTokenKeyPrefix, String userId) {
        redisUtils.delete(invalidTokenKeyPrefix+userId);
    }


    /**
     * 增加稿件的浏览量 (存储在Hash中)。
     * @param manuscriptId 稿件ID。
     */
    public void incrementManuscriptView(String manuscriptId) {
        redisUtils.hincr(Constants.REDIS_KEY_MANUSCRIPT_VIEWS, manuscriptId, 1);
    }

    /**
     * 获取所有待处理的稿件浏览量。
     * @return 一个Map，键是稿件ID，值是待增加的浏览量。
     */
    public Map<Object, Object> getAndClearManuscriptViews() {
        Map<Object, Object> views = redisUtils.hgetAll(Constants.REDIS_KEY_MANUSCRIPT_VIEWS);
        if (views != null && !views.isEmpty()) {
            redisUtils.delete(Constants.REDIS_KEY_MANUSCRIPT_VIEWS);
        }
        return views;
    }



    // --- 视频排行榜方法 ---

    /**
     * 增加视频在排行榜ZSET中的分数。
     * 第一次增加分数时，会自动设置3天的过期时间。
     *
     * @param manuscriptId 稿件(视频)ID。
     * @param scoreToAdd   要增加的分数。
     */
    public void incrementVideoScore(String manuscriptId, double scoreToAdd) {
        boolean isNew = !redisUtils.keyExists(Constants.REDIS_KEY_VIDEO_RANKING_ZSET);
        redisUtils.zsetIncrement(Constants.REDIS_KEY_VIDEO_RANKING_ZSET, manuscriptId, scoreToAdd);
        if (isNew) {
            redisUtils.expire(Constants.REDIS_KEY_VIDEO_RANKING_ZSET, Constants.REDIS_KEY_EXPIRES_THREE_DAYS);
        }
    }

    /**
     * 从排行榜ZSET中分页获取视频ID。
     *
     * @param page 页码 (从1开始)。
     * @param size 每页大小。
     * @return 稿件ID列表。
     */
    public List<String> getRankedVideoIds(int page, int size) {
        long start = (long) (page - 1) * size;
        long end = start + size - 1;
        return redisUtils.getZSetRange(Constants.REDIS_KEY_VIDEO_RANKING_ZSET, start, end);
    }

    // --- 用户观看历史记录方法 ---

    /**
     * 将一个视频添加到用户的观看历史记录中。
     * 历史记录存储在一个List中，并被裁剪为最新的100条记录。
     * 重复浏览同一视频时，会先删除旧记录，再将其添加到列表开头，确保记录不重复。
     *
     * @param userId       用户ID。
     * @param manuscriptId 要添加的稿件ID。
     */
    public void addUserViewHistory(String userId, String manuscriptId) {
        String key = Constants.REDIS_KEY_USER_VIEW_HISTORY + userId;
        
        // 先删除可能存在的同一ID记录（去重）
        redisUtils.remove(key, manuscriptId);
        
        // 再添加到列表开头
        redisUtils.lpush(key, manuscriptId, -1); // 用户历史记录不设置过期时间
        
        // 仅保留列表的指定范围
        redisUtils.ltrim(key, 0, Constants.USER_VIEW_HISTORY_MAX_SIZE - 1);
    }

    public List<String> getHotKeywords() {
        return (List<String>) redisUtils.get(Constants.REDIS_KEY_PRE+"hot_keywords");
    }

    public void deleteBlackUser(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_PRE+"user:black:"+userId);
    }

    public void setBlackUser(String userId) {
        redisUtils.setex(Constants.REDIS_KEY_PRE+"user:black:"+userId,userId,Constants.REDIS_KEY_EXPIRES_ONE_DAY*7);
    }

    public String getBlackUser(String userId) {
        return (String) redisUtils.get(Constants.REDIS_KEY_PRE+"user:black:"+userId);
    }
    
    // 存储用户信息到Redis，使用七天过期时间
    public void saveUserInfo(String token, Object userInfo) {
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, userInfo, Constants.REDIS_KEY_EXPIRES_SEVEN_DAYS);
    }
    
    // 从Redis获取用户信息
    public Object getUserInfo(String token) {
        return redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
    }
    
    // 删除用户信息
    public void deleteUserInfo(String token) {
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB + token);
    }
    
    // 检查用户token是否存在
    public boolean isUserTokenExists(String token) {
        return redisUtils.keyExists(Constants.REDIS_KEY_TOKEN_WEB + token);
    }
}
