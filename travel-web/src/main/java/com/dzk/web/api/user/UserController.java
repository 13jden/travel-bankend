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

}
