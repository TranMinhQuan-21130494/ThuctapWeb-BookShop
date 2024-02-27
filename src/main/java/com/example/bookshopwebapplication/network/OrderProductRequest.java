package com.example.bookshopwebapplication.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderProductRequest {
    private long productId;
    private double tempPrice;
    private Integer deliveryMethod;
    private Double deliveryPrice;
    private Integer quantity;
}
