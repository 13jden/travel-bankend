package com.dzk.common.redis;

import com.dzk.common.constants.Constants;


import com.dzk.web.api.auth.TokenUserDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RedisComponent {

    @Resource
    private com.dzk.common.redis.RedisUtils redisUtils;

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


    public String getBlackUser(String userId) {
        return (String) redisUtils.get(Constants.REDIS_KEY_PRE+"user:black:"+userId);
    }
    
    // 存储用户信息到Redis，使用七天过期时间
    public void saveUserToken(String token, Object userInfo) {
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB + token, userInfo, Constants.REDIS_KEY_EXPIRES_SEVEN_DAYS);
    }
    
    // 从Redis获取用户信息
    public TokenUserDto getUserToken(String token) {
        return (TokenUserDto)redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB + token);
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
