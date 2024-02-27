package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IWishlistItemDao;
import com.example.bookshopwebapplication.dao.mapper.WishlistItemMapper;
import com.example.bookshopwebapplication.entities.WishListItem;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class WishlistItemDao extends AbstractDao<WishListItem> implements IWishlistItemDao {

    // Phương thức để lưu một mục Wishlist mới vào cơ sở dữ liệu
    public Long save(WishListItem wishListItem) {
        // Thiết lập câu truy vấn mới
        clearSQL();
        builderSQL.append(
                "INSERT INTO wishlist_item (userId, productId, createdAt) " +
                        "VALUES(?, ?, ?)"
        );
        return insert(builderSQL.toString(), wishListItem.getUserId(),
                wishListItem.getProductId(), new Timestamp(System.currentTimeMillis()));
    }

    // Phương thức để cập nhật thông tin một mục Wishlist trong cơ sở dữ liệu
    public void update(WishListItem wishListItem) {
        clearSQL();
        builderSQL.append(
                "UPDATE wishlist_item SET userId = ?," +
                        " productId = ?, createdAt = " + new Timestamp(System.currentTimeMillis()) +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), wishListItem.getUserId(),
                wishListItem.getProductId());
    }

    // Phương thức để xóa một mục Wishlist từ cơ sở dữ liệu dựa trên ID
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM wishlist_item WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Phương thức để lấy thông tin một mục Wishlist dựa trên ID
    public Optional<WishListItem> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item WHERE id = ?");
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), id);
        return wishListItems.isEmpty() ? Optional.empty() : Optional.ofNullable(wishListItems.get(0));
    }

    // Phương thức để lấy một phần danh sách mục Wishlist từ cơ sở dữ liệu
    public List<WishListItem> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new WishlistItemMapper());
    }

    // Phương thức để lấy một phần danh sách mục Wishlist từ cơ sở dữ liệu với sắp xếp
    public List<WishListItem> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM wishlist_item ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new WishlistItemMapper());
    }

    // Phương thức để đếm tổng số lượng mục Wishlist trong cơ sở dữ liệu
    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM wishlist_item"
        );
        return count(builderSQL.toString());
    }

    // Phương thức để lấy danh sách mục Wishlist dựa trên ID người dùng
    @Override
    public List<WishListItem> getByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM wishlist_item WHERE userId = ?"
        );
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), userId);
        return wishListItems.isEmpty() ? new LinkedList<>() : wishListItems;
    }

    // Phương thức để đếm số lượng mục Wishlist dựa trên ID người dùng và ID sản phẩm
    @Override
    public List<WishListItem> getOrderedPartByUserId(long userId, Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM wishlist_item " +
                        "WHERE userId = ? " +
                        "ORDER BY " + orderBy + " " + sort + " " +
                        "LIMIT " + offset + ", " + limit + " "
        );
        List<WishListItem> wishListItems = query(builderSQL.toString(), new WishlistItemMapper(), userId);
        return wishListItems.isEmpty() ? new LinkedList<>() : wishListItems;
    }

    @Override
    public int countByUserIdAndProductId(Long userId, Long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) " +
                        "FROM wishlist_item " +
                        "WHERE userId = ? AND productId = ?"
        );
        return count(builderSQL.toString(), userId, productId);
    }
}
