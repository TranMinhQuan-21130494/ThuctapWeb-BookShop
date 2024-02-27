package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.ICartDao;
import com.example.bookshopwebapplication.dao.mapper.CartMapper;
import com.example.bookshopwebapplication.dao.mapper.WishlistItemMapper;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.WishListItem;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CartDao extends AbstractDao<Cart> implements ICartDao {
    public Long save(Cart cart) {
        // set new Query
        clearSQL();
        builderSQL.append(
                "INSERT INTO cart (userId, createdAt, updatedAt) " +
                        "VALUES(?, ?, ?)"
        );
        return insert(builderSQL.toString(), new CartMapper(), cart.getUserId(), cart.getCreatedAt(),
                cart.getUpdatedAt());
    }
    // Cập nhật thông tin của một Cart đã tồn tại trong cơ sở dữ liệu
    public void update(Cart cart) {
        clearSQL();
        builderSQL.append(
                "UPDATE cart " +
                        "SET userId = ?, createdAt = ?, updatedAt = ? " +
                        "WHERE id = ?"
        );
        update(builderSQL.toString(), cart.getUserId(),
                cart.getCreatedAt(), new Timestamp(System.currentTimeMillis()), cart.getId());
    }

    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM cart WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    public Optional<Cart> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new CartMapper(), id).get(0));
    }

    public List<Cart> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart LIMIT " + offset + ", " + limit);
        List<Cart> carts = query(builderSQL.toString(), new CartMapper());
        return carts.isEmpty() ? new LinkedList<>() : carts;
    }

    //int limit, int offset, String orderBy, String sort
    public List<Cart> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM cart ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        List<Cart> carts = query(builderSQL.toString(), new CartMapper());
        return carts.isEmpty() ? new LinkedList<>() : carts;
    }

    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM cart"
        );
        return count(builderSQL.toString());
    }
    public Long getUserId(Long id){
        clearSQL();
        builderSQL.append(
            "SELECT userId FROM cart WHERE id = ?"
        );
        return getIdElement(builderSQL.toString(), id);
    }
    @Override
    public Optional<Cart> getByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM cart WHERE userId = ?"
        );
        List<Cart> carts = query(builderSQL.toString(), new CartMapper(), userId);
        return carts.isEmpty() ? Optional.empty() : Optional.ofNullable(carts.get(0));
    }

    @Override
    public int countCartItemQuantityByUserId(long userId) {
        // Đếm tổng số lượng Cart Item dựa trên userId
        clearSQL();
        builderSQL.append(
                "SELECT SUM(ci.quantity) " +
                        "FROM cart c " +
                        "JOIN cart_item ci " +
                        "ON c.id = ci.cartId " +
                        "WHERE c.userId = ?"
        );
        return count(builderSQL.toString(), userId);
    }

    @Override
    // Đếm số lượng đơn hàng dựa trên userId
    public int countOrderByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(orders.id) FROM orders WHERE userId = ?"
        );
        return count(builderSQL.toString(), userId);
    }

    @Override
    // Đếm số lượng đơn hàng đã giao dựa trên userId
    public int countOrderDeliverByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(orders.id)" +
                        " FROM orders" +
                        " WHERE userId = ? AND status = 1"
        );
        return count(builderSQL.toString(), userId);
    }

    @Override
    // Đếm số lượng đơn hàng đã nhận dựa trên userId
    public int countOrderReceivedByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(orders.id)" +
                        " FROM orders" +
                        " WHERE userId = ? AND status = 2"
        );
        return count(builderSQL.toString(), userId);
    }
}
