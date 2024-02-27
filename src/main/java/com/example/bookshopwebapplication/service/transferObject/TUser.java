package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;

public class TUser implements ITransfer<UserDto, User>{
    @Override
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setGender(user.getGender());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole());
        return userDto;
    }
    @Override
    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());
        return user;
    }
}
