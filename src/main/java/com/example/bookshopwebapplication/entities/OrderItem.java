package com.example.bookshopwebapplication.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private Double price;
    private Double discount;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Product product;
    private Order order;
    public OrderItem(Long id,
                     Long orderId,
                     Long productId,
                     Double price,
                     Double discount,
                     Integer quantity,
                     Timestamp createdAt,
                     Timestamp updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
