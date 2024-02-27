package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IUserDao;
import com.example.bookshopwebapplication.dao.mapper.UserMapper;
import com.example.bookshopwebapplication.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao extends AbstractDao<User> implements IUserDao {

    // Phương thức để lưu thông tin người dùng mới vào cơ sở dữ liệu
    public Long save(User user) {
        // Thiết lập câu truy vấn mới
        clearSQL();
        builderSQL.append("INSERT INTO user (username, password, fullname, email, phoneNumber, ");
        builderSQL.append("gender, address, role) ");
        builderSQL.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        return insert(builderSQL.toString(), user.getUsername(), user.getPassword(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.getRole());
    }

    // Phương thức để cập nhật thông tin người dùng trong cơ sở dữ liệu
    public void update(User user) {
        clearSQL();
        builderSQL.append("UPDATE user SET username = ?, password = ?, fullname = ?, email = ?, ");
        builderSQL.append("phoneNumber = ?, gender = ?, address = ?, role = ? ");
        builderSQL.append("WHERE id = ?");
        update(builderSQL.toString(), user.getUsername(), user.getPassword(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getGender(), user.getAddress(), user.getRole(),
                user.getId()
        );
    }

    // Phương thức để xóa người dùng từ cơ sở dữ liệu dựa trên ID
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM user WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Phương thức để lấy thông tin người dùng dựa trên ID
    public Optional<User> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE id = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), id);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    // Phương thức để lấy một phần danh sách người dùng từ cơ sở dữ liệu
    public List<User> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM user LIMIT " + offset + ", " + limit);
        return getPart(builderSQL.toString(), new UserMapper());
    }

    // Phương thức để lấy một phần danh sách người dùng từ cơ sở dữ liệu với sắp xếp
    public List<User> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM user ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + "");
        return super.getOrderedPart(builderSQL.toString(), new UserMapper());
    }

    // Phương thức để lấy thông tin người dùng dựa trên tên đăng nhập
    @Override
    public Optional<User> getByNameUser(String username) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE username = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), username);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    // Phương thức để thay đổi mật khẩu của người dùng dựa trên ID người dùng
    @Override
    public void changePassword(long userId, String newPassword) {
        clearSQL();
        builderSQL.append("UPDATE user SET password = ? WHERE id = ?");
        update(builderSQL.toString(), newPassword, userId);
    }

    // Phương thức để lấy thông tin người dùng dựa trên địa chỉ email
    @Override
    public Optional<User> getByEmail(String email) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE email = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), email);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    // Phương thức để lấy thông tin người dùng dựa trên số điện thoại
    @Override
    public Optional<User> getByPhoneNumber(String phoneNumber) {
        clearSQL();
        builderSQL.append("SELECT * FROM user WHERE phoneNumber = ?");
        List<User> users = query(builderSQL.toString(), new UserMapper(), phoneNumber);
        return users.isEmpty() ? Optional.empty() : Optional.ofNullable(users.get(0));
    }

    @Override
    public List<User> getAll() {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM user"
        );
        List<User> users = query(builderSQL.toString(), new UserMapper());
        return users.isEmpty() ? new ArrayList<>(): users;
    }

    // Phương thức để đếm tổng số lượng người dùng trong cơ sở dữ liệu
    public int count() {
        clearSQL();
        builderSQL.append("SELECT COUNT(*) FROM user");
        return count(builderSQL.toString());
    }
}
