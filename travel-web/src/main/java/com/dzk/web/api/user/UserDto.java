package com.dzk.web.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户数据传输对象")
public class UserDto {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "微信openId")
    private String openId;
    
    @Schema(description = "用户头像")
    private String avatar;
    
    @Schema(description = "用户角色")
    private RoleEnum role;
    
    @Schema(description = "JWT认证令牌")
    private String token;
    
    @Schema(description = "用户权限信息")
    private String authorities;

    @Schema(description = "用户语言习惯")
    private User.Language language;

    @Data
    @Schema(description = "用户输入数据传输对象")
    public static class Input {
        @Schema(description = "用户名")
        private String username;
        
        @Schema(description = "用户头像")
        private String avatar;

        @Schema(description = "用户语言习惯")
        private User.Language language;

        @Schema(description = "用户角色")
        private RoleEnum role;

        @Schema(description = "微信openId")
        private String openId;
    }
}
