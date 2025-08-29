package com.dzk.admin.interceptor;

import com.dzk.common.constants.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AppInterceptor implements HandlerInterceptor {

    private final static String URL_ACCOUNT = "/admin";
    private final static String URL_FILE = "/file";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if(null==handler){
//            return false;
//        }
//        if(!(handler instanceof HandlerMethod)){
//            return true;
//        }
//        if(request.getRequestURI().contains(URL_ACCOUNT)){
//            return true;
//        }
//        String token = request.getHeader(Constants.TOKEN_ADMIN);
//        //获取封面图
//        if(request.getRequestURI().contains(URL_FILE))
//            token = getTokenFromCookie(request);
//        System.out.println(request.getHeader(Constants.TOKEN_WEB));
//        if(StringTools.isEmpty(request.getHeader(Constants.TOKEN_WEB))){
//            throw new LoginTimeoutException("未登录");
//        }

        return true;
    }

    private  String getTokenFromCookie(HttpServletRequest request){
        String token = null;
        //清理上一条登录token
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(Constants.TOKEN_WEB)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
