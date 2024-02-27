package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.entities.Cart;

public class TCart implements ITransfer<CartDto, Cart>{
    @Override
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cart.setCreatedAt(cart.getCreatedAt());
        cart.setUpdatedAt(cart.getUpdatedAt());
        return cartDto;
    }

    @Override
    public Cart toEntity(CartDto dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setUserId(dto.getUser().getId());
        cart.setCreatedAt(dto.getCreatedAt());
        cart.setUpdatedAt(dto.getUpdatedAt());
        return cart;
    }
}
