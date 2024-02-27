package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.CartItem;
import com.example.bookshopwebapplication.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Data
@ToString
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CartItemDto> cartItems;
    private UserDto user;

    public CartDto(Long id, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cartItems = new ArrayList<>();
    }
}
