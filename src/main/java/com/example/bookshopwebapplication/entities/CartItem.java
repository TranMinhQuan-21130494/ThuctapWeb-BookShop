package com.example.bookshopwebapplication.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Product product;
    private Cart cart;
    public CartItem(Long id,
                    Long cartId,
                    Long productId,
                    Integer quantity,
                    Timestamp createdAt,
                    Timestamp updatedAt) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
