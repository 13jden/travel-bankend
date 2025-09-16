package com.dzk.web.api.auth;

import com.dzk.common.common.Result;
import com.dzk.common.exception.BusinessException;
import com.dzk.web.api.auth.captcha.CaptchaDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;



@RestController 
@RequestMapping("/api/auth")
@Tag(name = "AuthController", description = "认证控制器")
public class AuthController {

    @Autowired
    private AuthService authService;

    
    @PostMapping("/login")
    @Operation(summary = "登录", description = "登录", operationId = "Login")
    public Result<TokenUserDto> login(@RequestBody LoginRequest request) throws Exception {
        return Result.success(authService.login(request));
    }

    @GetMapping("/code")
    @Operation(summary = "获取图片验证码 默认为算术题", description = "获取图片验证码 默认为算术题", operationId = "GetCode")
    public Result<CaptchaDto> getCode() {
        CaptchaDto imgResult = authService.getCaptcha();

        return Result.success(imgResult);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "重置密码", operationId = "ResetPassword")
    public Result resetPassword(@RequestBody PasswordDto passwordDto) {
        return Result.success(authService.resetPassword(passwordDto));
    }

    @GetMapping("/verify")
    @Operation(summary = "验证验证码", description = "验证验证码", operationId = "VerifyCaptcha")
    public Result verifyCaptcha(@RequestParam String uuid, @RequestParam String code) {
        return Result.success(authService.verifyCaptcha(uuid,code ));
    }

}