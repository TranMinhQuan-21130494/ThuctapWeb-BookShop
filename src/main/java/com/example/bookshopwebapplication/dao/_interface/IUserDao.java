package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserDao extends IGenericDao<User>{
    Optional<User> getByNameUser(String username);
    void changePassword(long userId, String newPassword);
    Optional<User> getByEmail( String email);
    Optional<User> getByPhoneNumber(String phoneNumber);
    List<User> getAll();
}
