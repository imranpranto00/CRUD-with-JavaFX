package com.imranpranto.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection con;

    public static void getDBConn() {
        synchronized (DBConnection.class) {
            try {
                if (con == null || con.isClosed()) {
                    String url = "jdbc:mysql://localhost/student_list";
                    String user = "root";
                    String password = "root";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(url, user, password);
                }
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found. Add it to your library path.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database.");
                e.printStackTrace();
            }
        }
    }

    public static Connection getCon() {
        return con;
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
