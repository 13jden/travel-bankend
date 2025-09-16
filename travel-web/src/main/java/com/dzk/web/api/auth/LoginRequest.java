package com.dzk.web.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class LoginRequest {

    @Schema(description = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "验证码ID")
    private String uuid;
}