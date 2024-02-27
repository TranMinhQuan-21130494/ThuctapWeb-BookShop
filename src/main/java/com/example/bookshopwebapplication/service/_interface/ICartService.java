package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.entities.Cart;

import java.util.Optional;

public interface ICartService extends IService<CartDto> {
    public Optional<CartDto> getByUserId(long userId);

    public int countCartItemQuantityByUserId(long userId);

    public int countOrderByUserId(long userId);

    public int countOrderDeliverByUserId(long userId);

    public int countOrderReceivedByUserId(long userId);
}
