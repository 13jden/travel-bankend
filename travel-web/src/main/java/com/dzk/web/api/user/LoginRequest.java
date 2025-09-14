package com.dzk.web.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录请求对象")
public class LoginRequest {
    
    @Schema(description = "用户名", example = "admin")
    private String username;
    
    @Schema(description = "密码", example = "123456")
    private String password;
    
    @Schema(description = "验证码", example = "1234")
    private String checkCode;
    
    @Schema(description = "验证码标识key", example = "uuid-string")
    private String captchaKey;
} 