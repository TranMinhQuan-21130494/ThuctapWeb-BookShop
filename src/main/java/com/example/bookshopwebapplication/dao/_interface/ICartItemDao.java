package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemDao extends IGenericDao<CartItem> {
    List<CartItem> getByCartId(long cartId);

    Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) ;

    int sumQuantityByUserId(long userId);
}
