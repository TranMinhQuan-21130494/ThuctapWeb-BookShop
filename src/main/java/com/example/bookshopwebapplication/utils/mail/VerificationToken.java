package com.example.bookshopwebapplication.utils.mail;

import lombok.*;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationToken {
    private String email;
    private String code;
    private long expirationTime;
}
