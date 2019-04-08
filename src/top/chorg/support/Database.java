package top.chorg.support;

import java.sql.*;

public class Database {

    private String DATABASE = "jdbc:mysql://localhost:3306/g-class-manager?serverTimezone=GMT%2B8";
    private String USERNAME = "root";
    private String PASSWORD = "8835230.";

    private Connection connection;
    private Statement statement;
    public Database(String databaseJdbcAddress, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(databaseJdbcAddress, user, password);
        statement = connection.createStatement();
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public int update(String sql) throws SQLException {
        return statement.executeUpdate(sql);
    }

    public ResultSet query(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
