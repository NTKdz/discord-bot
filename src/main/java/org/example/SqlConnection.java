package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public SqlConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    // Connect to database
    public Connection connectToDatabase() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to the database.");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
        return connection;
    }

    // Close connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Connection closed.");
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            }
        }
    }

    // Optionally provide a getter for the connection
    public Connection getConnection() {
        return connection;
    }
}
