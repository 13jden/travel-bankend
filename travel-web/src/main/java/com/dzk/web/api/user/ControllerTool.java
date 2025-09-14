package com.dzk.web.api.user;

import com.dzk.common.constants.Constants;
import com.dzk.common.redis.RedisComponent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ControllerTool {

    @Resource
    private RedisComponent redisComponent;

    public  void saveToken2Cookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(Constants.TOKEN_WEB, token);
        cookie.setMaxAge(Constants.REDIS_KEY_EXPIRES_ONE_DAY*7 / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public  void saveTokenAdminCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(Constants.TOKEN_ADMIN, token);
        cookie.setMaxAge(Constants.REDIS_KEY_EXPIRES_ONE_DAY / 1000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }



    public void cleanCookie(HttpServletResponse response){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        String token = null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals((Constants.TOKEN_ADMIN)))
            {
                redisComponent.cleanToken(cookie.getValue());
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }

        }
    }
}
