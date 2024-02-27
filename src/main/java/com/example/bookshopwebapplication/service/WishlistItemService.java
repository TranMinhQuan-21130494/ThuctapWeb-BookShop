package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.WishlistItemDao;
import com.example.bookshopwebapplication.dto.WishlistItemDto;
import com.example.bookshopwebapplication.entities.WishListItem;
import com.example.bookshopwebapplication.service._interface.IWishlistItemService;
import com.example.bookshopwebapplication.service.transferObject.TWishlistItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WishlistItemService implements IWishlistItemService {
    private WishlistItemDao wishlistItemDao = new WishlistItemDao();
    private TWishlistItem tWishlistItem = new TWishlistItem();
    private static final WishlistItemService instance = new WishlistItemService();

    public static WishlistItemService getInstance() {
        return instance;
    }

    @Override
    public Optional<WishlistItemDto> insert(WishlistItemDto wishlistItemDto) {
        Long id = wishlistItemDao.save(tWishlistItem.toEntity(wishlistItemDto));
        return getById(id);
    }

    @Override
    public Optional<WishlistItemDto> update(WishlistItemDto wishlistItemDto) {
        wishlistItemDao.update(tWishlistItem.toEntity(wishlistItemDto));
        return getById(wishlistItemDto.getId());
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) wishlistItemDao.delete(id);
    }

    @Override
    public Optional<WishlistItemDto> getById(Long id) {
        Optional<WishListItem> wishListItem = wishlistItemDao.getById(id);
        if (wishListItem.isPresent()) {
            WishlistItemDto wishlistItemDto = tWishlistItem.toDto(wishListItem.get());
            wishlistItemDto.setUser(UserService.getInstance()
                    .getById(wishListItem.get().getUserId()).get());
            wishlistItemDto.setProduct(ProductService.getInstance()
                    .getById(wishListItem.get().getProductId()).get());
            return Optional.of(wishlistItemDto);
        }
        return Optional.empty();
    }

    @Override
    public List<WishlistItemDto> getPart(Integer limit, Integer offset) {
        return wishlistItemDao.getPart(limit, offset)
                .stream()
                .map(wishListItem -> getById(wishListItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<WishlistItemDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return wishlistItemDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(wishListItem -> getById(wishListItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return wishlistItemDao.count();
    }

    @Override
    public List<WishlistItemDto> getByUserId(long userId) {
        return wishlistItemDao.getByUserId(userId)
                .stream()
                .map(wishListItem -> getById(wishListItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<WishlistItemDto> getOrderedPartByUserId(long userId, Integer limit, Integer offset, String orderBy, String sort) {
        return wishlistItemDao.getOrderedPartByUserId(userId, limit, offset, orderBy, sort)
                .stream()
                .map(wishListItem -> getById(wishListItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public int countByUserIdAndProductId(Long userId, Long productId) {
        return wishlistItemDao.countByUserIdAndProductId(userId, productId);
    }
}
