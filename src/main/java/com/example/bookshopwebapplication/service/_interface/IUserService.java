package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<UserDto> {
    Optional<UserDto> getByUsername(String username);

    void changePassword(long userId, String newPassword);

    Optional<UserDto> getByEmail(String email);

    Optional<UserDto> getByPhoneNumber(String phoneNumber);
    List<String> getAllUsername();
    List<String> getAllEmails();
    List<String> getAllPhones();
}
