package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IProductReviewDao;
import com.example.bookshopwebapplication.dao.mapper.ProductReviewMapper;
import com.example.bookshopwebapplication.entities.ProductReview;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.text.SimpleDateFormat;

public class ProductReviewDao extends AbstractDao<ProductReview> implements IProductReviewDao {

    // Phương thức để lưu đánh giá sản phẩm mới vào cơ sở dữ liệu
    public Long save(ProductReview pr) {
        // Thiết lập câu truy vấn mới
        clearSQL();
        builderSQL.append(
                "INSERT INTO product_review (userId, productId, ratingScore, content, isShow" +
                        " createdAt, updatedAt) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), pr.getUserId(), pr.getProductId(), pr.getRatingScore(),
                pr.getContent(), pr.getIsShow(), pr.getCreatedAt(), new Timestamp(System.currentTimeMillis()));
    }

    // Phương thức để cập nhật thông tin đánh giá sản phẩm trong cơ sở dữ liệu
    public void update(ProductReview pr) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET ratingScore = ?, content = ?, " +
                        "updatedAt = " + new Timestamp(System.currentTimeMillis()) + " "
                        + "WHERE id = ?"
        );
        update(builderSQL.toString(), pr.getRatingScore(), pr.getContent(), pr.getUpdatedAt(), pr.getId());
    }

    // Phương thức để xóa đánh giá sản phẩm từ cơ sở dữ liệu
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM product_review WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Phương thức để lấy đánh giá sản phẩm dựa trên ID
    public Optional<ProductReview> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review WHERE id = ?");
        List<ProductReview> list = query(builderSQL.toString(), new ProductReviewMapper(), id);
        return list.isEmpty() ? null : Optional.ofNullable(list.get(0));
    }

    // Phương thức để lấy một phần danh sách đánh giá sản phẩm từ cơ sở dữ liệu
    public List<ProductReview> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new ProductReviewMapper());
    }

    // Phương thức để lấy một phần danh sách đánh giá sản phẩm từ cơ sở dữ liệu với sắp xếp
    public List<ProductReview> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM product_review ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new ProductReviewMapper());
    }

    // Phương thức để đếm số lượng đánh giá sản phẩm trong cơ sở dữ liệu
    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM product_review"
        );
        return count(builderSQL.toString());
    }

    // Phương thức để lấy một phần danh sách đánh giá sản phẩm từ cơ sở dữ liệu với sắp xếp theo sản phẩm và giới hạn số lượng
    @Override
    public List<ProductReview> getOrderedPartByProductId(int limit, int offset, String orderBy, String sort, long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT pr.*, u.fullname " +
                        "FROM product_review pr " +
                        "JOIN user u ON pr.userId = u.id " +
                        "WHERE productId = ? AND pr.isShow = 1 " +
                        "ORDER BY " + orderBy + " " + sort +
                        " LIMIT " + offset + ", " + limit
        );
        List<ProductReview> productReviews = query(builderSQL.toString(), new ProductReviewMapper(), productId);
        return productReviews.isEmpty() ? new LinkedList<>() : productReviews;
    }

    // Phương thức để đếm số lượng đánh giá sản phẩm dựa trên ID sản phẩm
    @Override
    public int countByProductId(long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) FROM product_review WHERE productId = ?"
        );
        return count(builderSQL.toString(), productId);
    }

    // Phương thức để tính tổng điểm đánh giá sản phẩm dựa trên ID sản phẩm
    @Override
    public int sumRatingScoresByProductId(long productId) {
        clearSQL();
        builderSQL.append(
                "SELECT SUM(ratingScore) FROM product_review WHERE productId = ?"
        );
        return count(builderSQL.toString(), productId);
    }

    // Phương thức để ẩn một đánh giá sản phẩm dựa trên ID
    @Override
    public void hide(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET isShow = 0," +
                        " updatedAt = NOW()" +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    // Phương thức để hiển thị một đánh giá sản phẩm dựa trên ID
    @Override
    public void show(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_review SET isShow = 1," +
                        " updatedAt = NOW() " +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }
}
