package com.dzk.web.api.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.redis.RedisComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService  extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    
    @Autowired
    private UserConverter userConverter;
    
    @Autowired
    private RedisComponent redisComponent;

    public User getUserByOpenId(String openId) {
        return userMapper.getUserByOpenId(openId);
    }
    
    public User saveUser(User user) {
        int result = userMapper.insert(user);
        return userMapper.getUserByOpenId(user.getOpenId());
    }

    public User getUserByUserName(String username){
        return userMapper.getUserByUsername(username);
    }

    public UserDto login(String username, String password, String checkCode, String captchaKey) {
        // 验证验证码
        if (!validateCheckCode(captchaKey, checkCode)) {
            throw new RuntimeException("验证码错误");
        }
        
        // 根据用户名查询用户
        User user = userMapper.getUserByUsername(username);
        
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return userConverter.toDto(user);
//        }
        
        return null;
    }
    
    /**
     * 验证验证码
     */
    private boolean validateCheckCode(String captchaKey, String checkCode) {
        if (checkCode == null || checkCode.trim().isEmpty() || captchaKey == null || captchaKey.trim().isEmpty()) {
            return false;
        }
        
        // 从Redis中获取验证码进行验证
        String storedCode = redisComponent.getCheckCode(captchaKey);
        if (storedCode != null && storedCode.equalsIgnoreCase(checkCode)) {
            // 验证成功后删除验证码
            redisComponent.cleanCheckCode(captchaKey);
            return true;
        }
        
        return false;
    }
}
