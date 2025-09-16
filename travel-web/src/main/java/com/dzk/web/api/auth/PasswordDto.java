package com.dzk.web.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
@Builder
public class PasswordDto {

    @Schema(description = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @Schema(description = "新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;

}