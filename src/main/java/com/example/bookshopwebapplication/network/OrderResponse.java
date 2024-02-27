package com.example.bookshopwebapplication.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private long id;
    private String createdAt;
    private String name;
    private int status;
    private double total;
}
