package com.dzk.web.user;

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
        
        return userDto;
    }
    
    /**
     * 将UserDto转换为User实体
     */
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setOpenId(userDto.getOpenId());
        user.setAvatar(userDto.getAvatar());
        user.setRole(userDto.getRole());
        
        return user;
    }
} 