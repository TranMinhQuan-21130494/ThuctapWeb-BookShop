package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.WishlistItemDto;
import com.example.bookshopwebapplication.entities.WishListItem;

public class TWishlistItem implements ITransfer<WishlistItemDto, WishListItem> {
    @Override
    public WishlistItemDto toDto(WishListItem wishListItem) {
        WishlistItemDto wishlistItemDto = new WishlistItemDto();
        wishlistItemDto.setId(wishListItem.getId());
        wishlistItemDto.setCreatedAt(wishListItem.getCreatedAt());
        return wishlistItemDto;
    }

    @Override
    public WishListItem toEntity(WishlistItemDto wishlistItemDto) {
        WishListItem wishListItem = new WishListItem();
        wishListItem.setId(wishlistItemDto.getId());
        wishListItem.setUserId(wishlistItemDto.getUser().getId());
        wishListItem.setProductId(wishlistItemDto.getProduct().getId());
        wishListItem.setCreatedAt(wishlistItemDto.getCreatedAt());
        return wishListItem;
    }
}
