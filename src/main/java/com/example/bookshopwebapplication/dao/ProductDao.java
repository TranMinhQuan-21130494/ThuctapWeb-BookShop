package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IProductDao;
import com.example.bookshopwebapplication.dao.mapper.ProductMapper;
import com.example.bookshopwebapplication.entities.Product;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ProductDao extends AbstractDao<Product> implements IProductDao {

    // Phương thức để lưu một đối tượng Product vào cơ sở dữ liệu
    public Long save(Product product) {
        clearSQL();
        builderSQL.append("INSERT INTO product (name, price, discount, quantity, totalBuy, author, ");
        builderSQL.append("pages, publisher, yearPublishing, description, imageName, shop, createdAt, ");
        builderSQL.append("updatedAt) ");
        builderSQL.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return insert(builderSQL.toString(), product.getName(), product.getPrice(), product.getDiscount(),
                product.getQuantity(), product.getTotalBuy(), product.getAuthor(), product.getPages(),
                product.getPublisher(), product.getYearPublishing(), product.getDescription(),
                product.getImageName(), product.getShop(), product.getCreatedAt(), product.getUpdatedAt());
    }

    // Phương thức để cập nhật thông tin một đối tượng Product trong cơ sở dữ liệu
    public void update(Product product) {
        clearSQL();
        builderSQL.append(
                "UPDATE product SET name = ?, price = ?, discount = ?, quantity = ?, " +
                        "totalBuy = ?, author = ?, pages = ?, publisher = ?, " +
                        "yearPublishing = ?, description = ?, imageName = ?, " +
                        "shop = ?, createdAt = ?, updatedAt = ? " +
                        "WHERE id = ?"
        );
        update(builderSQL.toString(), product.getName(), product.getPrice(), product.getDiscount(),
                product.getQuantity(), product.getTotalBuy(), product.getAuthor(), product.getPages(),
                product.getPublisher(), product.getYearPublishing(), product.getDescription(),
                product.getImageName(), product.getShop(), product.getCreatedAt(),
                new Timestamp(System.currentTimeMillis()), product.getId());
    }

    // Phương thức để xóa một đối tượng Product khỏi cơ sở dữ liệu theo ID
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM product WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Phương thức để lấy một đối tượng Product từ cơ sở dữ liệu theo ID
    public Optional<Product> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM product WHERE id = ?");
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? Optional.empty() : Optional.ofNullable(products.get(0));
    }

    // Phương thức để lấy một phần danh sách Product từ cơ sở dữ liệu với giới hạn và vị trí bắt đầu
    public List<Product> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM product LIMIT " + offset + ", " + limit);
        return super.getPart(builderSQL.toString(), new ProductMapper());
    }

    // Phương thức để lấy một phần danh sách Product từ cơ sở dữ liệu với sắp xếp theo các thuộc tính được chỉ định
    public List<Product> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM product ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new ProductMapper());
    }

    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM product"
        );
        return count(builderSQL.toString());
    }

    // Phương thức để đếm số lượng sản phẩm theo ID của một danh mục
    public int countByCategoryId(Long id) {
        clearSQL();
        builderSQL.append("SELECT COUNT(productId) FROM product_category WHERE categoryId = ?");
        return count(builderSQL.toString(), id);
    }

    // Phương thức để đếm số lượng sản phẩm theo ID của một danh mục và các điều kiện lọc
    @Override
    public int countByCategoryIdAndFilters(Long id, String filtersQuery) {
        clearSQL();
        builderSQL.append("SELECT COUNT(p.id) ");
        builderSQL.append("FROM product_category pc ");
        builderSQL.append("JOIN product p ON pc.productId = p.id ");
        builderSQL.append("WHERE pc.categoryId = ? ");
        builderSQL.append("AND " + filtersQuery);
        return count(builderSQL.toString(), id);
    }

    @Override
    public int countByFilter(String filtersQuery) {
        clearSQL();
        builderSQL.append("SELECT COUNT(*) ");
        builderSQL.append("FROM product_category pc ");
        builderSQL.append("JOIN product p ON pc.productId = p.id ");
        builderSQL.append("WHERE pc.categoryId = ? ");
        builderSQL.append("AND " + filtersQuery);
        return count(builderSQL.toString());
    }

    @Override
    public String getIDByCategoriesName(String categoryNames) {
        clearSQL();
        builderSQL.append(
                "SELECT pc.productId FROM product_category pc " +
                        "JOIN category c ON pc.categoryId = c.id " +
                        "WHERE c.name in " + categoryNames
        );

        return builderSQL.toString();
    }

    @Override
    public List<Product> getProductByFilter(String filters) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM product p WHERE " + filters
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper());
        return products.isEmpty() ? new LinkedList<>() : products;
    }

    // Phương thức để lấy một phần danh sách Product từ cơ sở dữ liệu với sắp xếp theo các thuộc tính được chỉ định và theo ID của một danh mục
    @Override
    public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String sort, Long id) {
        clearSQL();
        builderSQL.append("SELECT p.* FROM product_category pc JOIN product p ");
        builderSQL.append("ON pc.productId = p.id WHERE pc.categoryId = ? ");
        builderSQL.append("ORDER BY p." + orderBy + " " + sort + " ");
        builderSQL.append("LIMIT " + offset + ", " + limit);
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? null : products;
    }

    // Phương thức để lấy một phần danh sách Product từ cơ sở dữ liệu với sắp xếp theo các thuộc tính được chỉ định, theo ID của một danh mục và theo điều kiện lọc
    @Override
    public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String sort, Long id, String filtersQuery) {
        clearSQL();
        builderSQL.append(
                "SELECT p.* " +
                        "FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "AND " + filtersQuery +
                        "ORDER BY p." + orderBy + " " + sort + " " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), id);
        return products.isEmpty() ? new LinkedList<Product>() : products;
    }
    @Override
    public List<Product> getOrderedPartByFilters(int limit, int offset, String orderBy, String sort, String filters){
        clearSQL();
        builderSQL.append(
                "SELECT p.* " +
                        "FROM product p " +
                        "WHERE " + filters + " " +
                        "ORDER BY p." + orderBy + " " + sort + " " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper());
        return products.isEmpty() ? new LinkedList<>() : products;
    }
    // Phương thức để lấy danh sách các nhà xuất bản từ cơ sở dữ liệu theo ID của một danh mục
    @Override
    public List<String> getPublishersByCategoryId(Long id) {
        clearSQL();
        builderSQL.append(
                "SELECT DISTINCT p.publisher " +
                        "FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "ORDER BY p.publisher"
        );
        try {
            connection = getConnection();
            List<String> result = new LinkedList<>();
            statement = connection.prepareStatement(builderSQL.toString());
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("publisher"));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo đóng các tài nguyên liên quan đến cơ sở dữ liệu
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                return null;
            }
        }
    }

    // Phương thức để lấy một phần danh sách Product từ cơ sở dữ liệu với sắp xếp ngẫu nhiên, theo ID của một danh mục
    @Override
    public List<Product> getRandomPartByCategoryId(int limit, int offset, Long categoryId) {
        clearSQL();
        builderSQL.append(
                "SELECT p.* FROM product_category pc " +
                        "JOIN product p ON pc.productId = p.id " +
                        "WHERE pc.categoryId = ? " +
                        "ORDER BY RAND() " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), categoryId);
        return products.isEmpty() ? new LinkedList<>() : products;
    }

    // Phương thức để lấy danh sách Product từ cơ sở dữ liệu theo ID của một danh mục
    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        clearSQL();
        builderSQL.append("SELECT p.* FROM product_category pc JOIN product p ");
        builderSQL.append("ON pc.productId = p.id WHERE pc.categoryId = ? ");
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), categoryId);
        return products.isEmpty() ? new LinkedList<Product>() : products;
    }

    @Override
    public List<String> getPublishers() {
        clearSQL();
        builderSQL.append("SELECT DISTINCT(publisher) FROM product");
        try {
            connection = getConnection();
            List<String> result = new LinkedList<>();
            statement = connection.prepareStatement(builderSQL.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("publisher"));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo đóng các tài nguyên liên quan đến cơ sở dữ liệu
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                return null;
            }
        }
    }

    @Override
    public int countByQuery(String query) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) " +
                        "FROM product " +
                        "WHERE name LIKE CONCAT('%', ?, '%')"
        );
        return count(builderSQL.toString(), query);
    }

    @Override
    public List<Product> getByQuery(String query, int limit, int offset) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM product " +
                        "WHERE name LIKE CONCAT('%', ?, '%') " +
                        "LIMIT " + limit + " OFFSET " + offset
        );
        List<Product> products = query(builderSQL.toString(), new ProductMapper(), query);
        return products.isEmpty() ? new LinkedList<>() : products;
    }

    @Override
    public void insertProductCategory(long productId, long categoryId) {
        clearSQL();
        builderSQL.append(
                "INSERT product_category (productId, categoryId) VALUES (?, ?)"
        );
        insertNoGenerateKey(builderSQL.toString(), productId, categoryId);
    }

    @Override
    public void updateProductCategory(long productId, long categoryId) {
        clearSQL();
        builderSQL.append(
                "UPDATE product_category SET categoryId = ? WHERE productId = ?"
        );
        update(builderSQL.toString(), categoryId, productId);
    }

    @Override
    public void deleteProductCategory(long productId, long categoryId) {
        clearSQL();
        builderSQL.append(
                "DELETE FROM product_category WHERE productId = ? AND categoryId = ?"
        );
        update(builderSQL.toString(), productId, categoryId);
    }
}
