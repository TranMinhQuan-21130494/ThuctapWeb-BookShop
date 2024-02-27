package com.example.bookshopwebapplication.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CartItemRequest {
    private final long userId;
    private final long productId;
    private final int quantity;

}
