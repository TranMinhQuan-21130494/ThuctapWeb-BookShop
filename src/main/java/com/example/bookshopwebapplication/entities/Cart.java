package com.example.bookshopwebapplication.entities;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class Cart {
    private Long id;
    private Long userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CartItem> cartItems;
    private User user;

    public Cart(Long id, Long userId, Timestamp createdAt, @NonNull Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cartItems = new ArrayList<>();
    }
}
