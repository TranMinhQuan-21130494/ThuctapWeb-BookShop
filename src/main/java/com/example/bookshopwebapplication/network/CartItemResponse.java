package com.example.bookshopwebapplication.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class CartItemResponse {
    private long id;
    private long cartId;
    private long productId;
    private String productName;
    private double productPrice;
    private double productDiscount;
    private int productQuantity;
    private String productImageName;
    private int quantity;
}
