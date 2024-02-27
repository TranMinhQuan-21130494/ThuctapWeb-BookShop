package com.example.bookshopwebapplication.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class Order {
    private Long id;
    private Long userId;
    private Integer status;
    private Integer deliveryMethod;
    private Double deliveryPrice;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User user;
    private List<OrderItem> orderItems;
    private double totalPrice;

    public Order(Long id,
                 Long userId,
                 Integer status,
                 Integer deliveryMethod,
                 Double deliveryPrice,
                 Timestamp createdAt,
                 Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.deliveryMethod = deliveryMethod;
        this.deliveryPrice = deliveryPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = new ArrayList<>();
    }
}
