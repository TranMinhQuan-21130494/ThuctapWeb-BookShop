package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.Cart;

import java.util.Optional;

public interface ICartDao extends IGenericDao<Cart>{
    public Optional<Cart> getByUserId(long userId);

    public int countCartItemQuantityByUserId(long userId);

    public int countOrderByUserId(long userId);

    public int countOrderDeliverByUserId(long userId);

    public int countOrderReceivedByUserId(long userId);
}
