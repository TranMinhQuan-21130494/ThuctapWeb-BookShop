package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.CartItemDto;
import com.example.bookshopwebapplication.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemService extends IService<CartItemDto>{
    List<CartItemDto> getByCartId(long cartId);

    Optional<CartItemDto> getByCartIdAndProductId(long cartId, long productId) ;

    int sumQuantityByUserId(long userId);
}
