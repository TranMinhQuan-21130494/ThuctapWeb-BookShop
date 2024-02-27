package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@ToString
public class WishlistItemDto {
    private Long id;
    private Timestamp createdAt;
    private ProductDto product;
    private UserDto user;
    public WishlistItemDto(Long id,
                        Timestamp createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
