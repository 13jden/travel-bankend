package com.dzk.web.api.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dzk.common.common.Result;
import com.dzk.common.constants.Constants;
import com.dzk.common.exception.BusinessException;
import com.dzk.common.exception.ValidationException;
import com.dzk.web.api.auth.captcha.CaptchaCodeType;
import com.dzk.web.api.auth.captcha.CaptchaProvider;
import com.dzk.web.api.user.User;
import com.dzk.web.api.user.UserService;
import com.dzk.web.utils.JwtUtil;
import com.wf.captcha.base.Captcha;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.dzk.web.api.auth.captcha.CaptchaDto;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private CaptchaProvider captchaProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private com.dzk.common.redis.RedisComponent redisComponent;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.captcha-enabled:true}")
	private boolean captchaEnabled;


    TokenUserDto login(LoginRequest request) throws Exception {
        if(captchaEnabled){
            String code = redisComponent.getCheckCode(request.getUuid());
            if (code==null){
                throw new BusinessException("验证码已失效，请重新获取");
            }
            if(!code.equals(request.getCode())){
                throw new ValidationException("验证码错误");
            }
        }
        User user = userService.getUserByUserName(request.getUsername());
        if(user == null){
            throw new BusinessException("用户不存在");
        }
        if(!user.getPassword().equals(request.getPassword())){
            throw new BusinessException("用户名或密码错误");
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getUsername());
        
        // 存储用户信息到Spring Security上下文
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(
                user,  // 存储完整的用户对象
                null,  // 密码设为null
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 存储到Redis
        TokenUserDto tokenUserDto = new TokenUserDto().toTokenUserDto(user, token);
        redisComponent.saveUserToken(Constants.REDIS_KEY_TOKEN_WEB+token, tokenUserDto);
        
        return tokenUserDto;
    }

    Result<String> logout(){
        return Result.success("退出成功");
    }

    CaptchaDto getCaptcha(){
		// 获取运算的结果
		Captcha captcha = captchaProvider.getCaptcha();
		String uuid = UUID.randomUUID().toString();
		//当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
		String captchaValue = captcha.text();
		if (captcha.getCharType() - 1 == CaptchaCodeType.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
			captchaValue = captchaValue.split("\\.")[0];
		}
		// 保存
		redisComponent.saveCheckCode(uuid, captchaValue);

		// 验证码信息
		return CaptchaDto.builder()
                .img(captcha.toBase64())
                .uuid(uuid)
                .build();
    }

    Result<String> resetPassword(PasswordDto passwordDto){
        return Result.success("重置密码成功");
    };

    public String verifyCaptcha(String uuid, String captchaCode) {
        String code = redisComponent.getCheckCode(uuid);
        if (code==null){
            throw new BusinessException("验证码已失效，请重新获取");
        }
        if (!code.equals(captchaCode)){
            throw new BusinessException("验证码错误");
        }
        return "验证成功！";
    }


}