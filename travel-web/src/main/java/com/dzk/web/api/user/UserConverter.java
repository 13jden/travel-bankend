package com.dzk.web.api.user;

import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    
    /**
     * 将User实体转换为UserDto
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setOpenId(user.getOpenId());
        userDto.setAvatar(user.getAvatar());
        userDto.setRole(user.getRole());
        userDto.setLanguage(user.getLanguage());
        return userDto;
    }
    
    /**
     * 将UserDto转换为User实体
     */
    public User toEntity(UserDto.Input input) {
        if (input == null) {
            return null;
        }
        
        User user = new User();
        user.setUsername(input.getUsername());
        user.setOpenId(input.getOpenId());
        user.setAvatar(input.getAvatar());
        user.setRole(input.getRole());
        user.setLanguage(input.getLanguage());
        return user;
    }
} 