package com.dzk.web.api.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dzk.common.common.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("user")
public class User extends BaseEntity {

  @TableId(type = IdType.AUTO)
  @Schema(description = "用户ID")
  private Long id;


  @Schema(description = "用户名")
  private String username;

  @Schema(description = "openId")
  private String openId;

  @Schema(description = "头像")
  private String avatar;

  @Schema(description = "昵称")
  private String nickname;

  @Schema(description = "电话号码")
  private String phone;

  @Schema(description = "密码")
  private String password;

  @Schema(description = "角色")
  private RoleEnum role;

  @Schema(description = "用户语言习惯")
  private Language language;

  public enum Language {
    ZH_CN,
    EN_US
  }

}
