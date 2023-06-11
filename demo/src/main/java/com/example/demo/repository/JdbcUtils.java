package com.example.demo.repository;

import java.sql.*;

public class JdbcUtils {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/abdpv_np?stringtype=unspecified";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection connection;

    private final static String USER = "postgres";
    private final static String PASSWORD = "12345";

    private static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sqlExpression) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sqlExpression, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return preparedStatement;
    }
}
