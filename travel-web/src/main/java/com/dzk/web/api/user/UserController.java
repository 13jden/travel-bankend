package com.dzk.web.api.user;

import com.dzk.common.redis.RedisComponent;
import com.dzk.web.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisComponent redisComponent;

    @Value("${upload.avatar}")
    private String avatarPath;

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequest loginRequest) throws Exception {
        // 验证用户登录逻辑
        UserDto userDto = userService.login(
            loginRequest.getUsername(), 
            loginRequest.getPassword(), 
            loginRequest.getCheckCode(),
            loginRequest.getCaptchaKey()
        );
        
        if (userDto != null) {
            // 生成JWT token
            String token = jwtUtil.generateToken(userDto.getUsername());
            userDto.setToken(token);
            
            // 存储到Redis，使用七天过期时间
            redisComponent.saveUserInfo(token, userDto);
            
            return userDto;
        }
        
        throw new RuntimeException("用户名或密码错误");
    }
}
