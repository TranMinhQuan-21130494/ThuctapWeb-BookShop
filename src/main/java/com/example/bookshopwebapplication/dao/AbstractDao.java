package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IGenericDao;
import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.*;

//thực hiện các truy vấn và cập nhật dữ liệu.
public abstract class AbstractDao<T> implements IGenericDao<T> {
    protected Connection connection;
    protected PreparedStatement statement;
    protected ResultSet resultSet;
    protected ResourceBundle bundle = ResourceBundle.getBundle("database"); // get file db.properties
    protected StringBuilder builderSQL = new StringBuilder();

    public void clearSQL() {
        builderSQL.delete(0, builderSQL.length());
        builderSQL = new StringBuilder();
    }
    //kết nối csdl
    protected Connection getConnection() {
        try {
            Class.forName(bundle.getString("driverName"));
            Connection c = DriverManager.getConnection(bundle.getString("url"),
                    bundle.getString("user"), bundle.getString("password"));
            return c;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Null there here");
            throw new RuntimeException(e);
        }
    }

    @Override
    //thực hiện một truy vấn SQL SELECT và trả về một danh sách các đối tượng được ánh xạ từ kết quả truy vấn
    public List<T> query(String sql, IRowMapper<T> rowMapper, Object... parameters) {
        try {
            connection = getConnection();
            List<T> result = new LinkedList<>();
            statement = connection.prepareStatement(sql);
            setParameters(parameters);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(rowMapper.mapRow(resultSet));
            }
            statement.close();
            resultSet.close();
            connection.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    // thực hiện một truy vấn SQL UPDATE hoặc DELETE để cập nhật hoặc xóa dữ liệu trong cơ sở dữ liệu
    public void update(String sql, Object... parameters) {
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            setParameters(parameters);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            if (resultSet != null) try {
                resultSet.close();
            } catch (SQLException ignore) {
            }
            if (statement != null) try {
                statement.close();
            } catch (SQLException ignore) {
            }
            if (connection != null) try {
                connection.close();
            } catch (SQLException ignore) {
            }
        }
    }

    @Override
    //thực hiện một truy vấn SQL INSERT để chèn dữ liệu vào cơ sở dữ liệu
    public Long insert(String sql, Object... parameters) {
        try {
            Long id = null;
            connection = getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(parameters);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
                connection.commit();
            }
            return id;
        } catch (SQLException e) {
            try {
                if (!Objects.isNull(connection)) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
            return null;
        } finally {
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
                throw new RuntimeException(e);
            }
        }
    }
    public void insertNoGenerateKey(String sql, Object... parameters){
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(sql);
            setParameters(parameters);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (!Objects.isNull(connection)) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        } finally {
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
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    //hực hiện một truy vấn SQL SELECT để lấy một đối tượng theo ID.trả về một Optional chứa đối tượng nếu tìm thấy, hoặc null nếu không tìm thấy
    public Optional<T> getById(String sql, IRowMapper<T> mapper, Object... parameters) {
        List<T> list = query(sql, mapper, parameters);
        return list.isEmpty() ? Optional.empty() : (Optional<T>) list.get(0);
    }

    @Override
    //Phương thức này thực hiện một truy vấn SQL SELECT để lấy tất cả các đối tượng.trả về một danh sách các đối tượng được ánh xạ từ kết quả truy vấn
    public List<T> getAll(String sql, IRowMapper<T> mapper, Object... parameters) {
        return query(sql, mapper, parameters);
    }

    @Override
    //tương tự getAll
    public List<T> getPart(String sql, IRowMapper<T> mapper, Object... parameters) {
        return query(sql, mapper, parameters);
    }

    @Override

    //int limit, int offset, String orderBy, String orderDir
    public List<T> getOrderedPart(String sql, IRowMapper<T> mapper, Object... parameters) {
        return query(sql, mapper, parameters);
    }

    @Override
    public int count(String sql, Object... parameters) {
        try {
            int count = 0;
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            setParameters(parameters);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException();
        } finally {
            try {
                statement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Long getIdElement(String sql, Object... parameters) {
        try {
            connection = this.getConnection();
            statement = connection.prepareStatement(sql);
            setParameters(parameters);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
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
                throw new RuntimeException(e);
            }
        }
        return 0L;
    }

    private void setParameters(Object... parameters) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                int index = i + 1;
                if (parameter instanceof Long) {
                    statement.setLong(index, (Long) parameter);
                } else if (parameter instanceof String) {
                    statement.setString(index, (String) parameter);
                } else if (parameter instanceof Integer) {
                    statement.setInt(index, (Integer) parameter);
                } else if (parameter instanceof Timestamp) {
                    statement.setTimestamp(index, (Timestamp) parameter);
                } else if (parameter instanceof Double) {
                    statement.setDouble(index, (Double) parameter);
                } else if (Objects.isNull(parameter)) {
                    statement.setNull(index, Types.NULL);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
