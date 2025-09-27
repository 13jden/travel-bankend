package com.dzk.web.config;

import com.dzk.web.redis.RedisComponent;
import com.dzk.web.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 放行登录、注册、验证码等接口
        if (request.getRequestURI().contains("/user/login") ||
            request.getRequestURI().contains("/user/register") ||
            request.getRequestURI().contains("/user/autologin") ||
            request.getRequestURI().contains("/checkcode") ||
            request.getRequestURI().contains("/captcha")) {
            return true;
        }

        // 从请求头获取token
        String token = request.getHeader("Authorization");

        System.out.println(token+"11111111");

        // 如果请求头中没有，尝试从请求参数获取
        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }

        if (!StringUtils.hasText(token)) {
            throw new RuntimeException("未登录，请先登录");
        }

        System.out.println(token);
        // 验证JWT token是否有效
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("token无效");
        }

//        // 从Redis获取用户信息
//        TokenUserInfoDto userInfo = redisComponent.getTokenInfo(jwtUtil.getUserId(token));
//        if (userInfo == null) {
//            throw new RuntimeException("token已过期，请重新登录");
//        }
//
//        // 检查是否需要延长token有效期
//        long currentTime = System.currentTimeMillis();
//        // 如果token有效期小于1天，就延长有效期
//        if (userInfo.getExpireAt() - currentTime < Constants.REDIS_KEY_EXPIRES_ONE_DAY) {
//            // 重新设置过期时间
//            userInfo.setExpireAt(currentTime + Constants.REDIS_KEY_EXPIRES_ONE_DAY * 7);
//            // 更新Redis中的记录
//            redisComponent.saveTokenUserInfo(userInfo);
//        }
//
//        // 将用户信息存入request属性中，方便后续使用
//        request.setAttribute("userInfo", userInfo);

        return true;
    }
}