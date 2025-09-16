package com.dzk.web.api.auth;

import com.dzk.web.api.user.RoleEnum;
import com.dzk.web.api.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenUserDto {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "角色")
    private RoleEnum role;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "token")
    private String token;

    public  TokenUserDto toTokenUserDto(User user, String token){
        return TokenUserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .role(user.getRole())
            .nickname(user.getNickname())
            .avatar(user.getAvatar())
            .phone(user.getPhone())
            .token(token)
            .build();
    }
}