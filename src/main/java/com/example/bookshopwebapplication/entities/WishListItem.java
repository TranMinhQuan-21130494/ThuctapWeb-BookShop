package com.example.bookshopwebapplication.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@ToString
public class WishListItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Timestamp createdAt;
    private Product product;
    private User user;
    public WishListItem(Long id,
                        Long userId,
                        Long productId,
                        Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }
}
