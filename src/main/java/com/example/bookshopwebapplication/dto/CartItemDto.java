package com.example.bookshopwebapplication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ProductDto product;
    private CartDto cart;

    public CartItemDto(Long id, Integer quantity, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
