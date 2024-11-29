package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConfig {
    static final String URL = "jdbc:mysql://localhost:3307/crm_app";
    static final String USER = "root";
    static final String PASSWORD = "admin123";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (Exception e) {
            System.out.println("Connection: " + e.getMessage());
        }
        return connection;
    }
}
