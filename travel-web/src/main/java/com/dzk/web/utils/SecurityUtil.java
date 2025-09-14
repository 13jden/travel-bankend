package com.dzk.web.utils;

import com.dzk.web.api.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public  class SecurityUtil {

    public static String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication();
    }

}   