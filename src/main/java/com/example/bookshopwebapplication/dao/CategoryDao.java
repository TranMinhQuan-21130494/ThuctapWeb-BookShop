package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.ICategoryDao;
import com.example.bookshopwebapplication.dao.mapper.CategoryMapper;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.entities.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CategoryDao extends AbstractDao<Category> implements ICategoryDao {

    // Lưu một Category mới vào cơ sở dữ liệu.
    public Long save(Category category) {
        clearSQL();
        builderSQL.append("INSERT INTO category (name, description, imageName) ");
        builderSQL.append("VALUES (?, ?, ?)");
        return insert(builderSQL.toString(), category.getName(), category.getDescription(),
                category.getImageName());
    }

    //Cập nhật thông tin của một Category trong cơ sở dữ liệu.
    public void update(Category category) {
        clearSQL();
        builderSQL.append("UPDATE category SET name = ?, description = ?, imageName = ? ");
        builderSQL.append("WHERE id = ?");
        update(builderSQL.toString(), category.getName(), category.getDescription(), category.getImageName(),
                category.getId());
    }

    // Xóa một Category khỏi cơ sở dữ liệu dựa trên id.
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM category WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Lấy danh sách tất cả các Category từ cơ sở dữ liệu.
    @Override
    public List<Category> getAll() {
        clearSQL();
        builderSQL.append("SELECT * FROM category");
        return super.query(builderSQL.toString(), new CategoryMapper());
    }

    // Lấy Category dựa trên id của Product.
    @Override
    public Optional<Category> getByProductId(long id) {
        clearSQL();
        builderSQL.append(
                "SELECT c.* " +
                        "FROM product_category pc " +
                        "JOIN category c ON pc.categoryId = c.id " +
                        "WHERE productId = ?"
        );
        List<Category> categories = query(builderSQL.toString(), new CategoryMapper(), id);
        return categories.isEmpty() ? null : Optional.ofNullable(categories.get(0));
    }

    // danh sách các Category với giới hạn số lượng và vị trí bắt đầu.
    public List<Category> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM category LIMIT " + offset + ", " + limit);
        return query(builderSQL.toString(), new CategoryMapper());
    }

    //Lấy danh sách các Category được sắp xếp theo một trường và thứ tự cụ thể.
    public List<Category> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM category ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return query(builderSQL.toString(), new CategoryMapper());
    }
    public int count(){
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM category"
        );
        return count(builderSQL.toString());
    }

    //Lấy Category dựa trên id.
    public Optional<Category> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM category WHERE id = ?");
        List<Category> list = query(builderSQL.toString(), new CategoryMapper(), id);
        return list.isEmpty() ? null : Optional.ofNullable(list.get(0));
    }
}