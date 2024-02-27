package com.example.bookshopwebapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer gender;
    private String address;
    private String role;
}
