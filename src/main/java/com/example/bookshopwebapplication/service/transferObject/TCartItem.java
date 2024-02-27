package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.CartItemDto;
import com.example.bookshopwebapplication.entities.CartItem;

public class TCartItem implements ITransfer<CartItemDto, CartItem> {
    @Override
    public CartItemDto toDto(CartItem entity) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(entity.getId());
        cartItemDto.setQuantity(entity.getQuantity());
        cartItemDto.setCreatedAt(entity.getCreatedAt());
        cartItemDto.setUpdatedAt(entity.getUpdatedAt());
        return cartItemDto;
    }

    @Override
    public CartItem toEntity(CartItemDto dto) {
        CartItem cartItem = new CartItem();
        cartItem.setId(dto.getId());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setCartId(dto.getCart().getId());
        cartItem.setProductId(dto.getProduct().getId());
        cartItem.setCreatedAt(dto.getCreatedAt());
        cartItem.setUpdatedAt(dto.getUpdatedAt());
        return cartItem;
    }
}
