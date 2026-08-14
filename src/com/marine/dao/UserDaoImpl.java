package com.marine.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.marine.bean.UserBean;

public class UserDaoImpl implements UserDao {

    private final String URL = "jdbc:mysql://localhost:3306/srs";
    private final String USER = "root";
    private final String PASSWORD = "root";

    // ---------------- Register User ----------------
    @Override
    public boolean registerUser(UserBean user) {
        boolean result = false;
        String sql = "INSERT INTO SRS_TBL_User (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ---------------- Validate Login ----------------
    @Override
    public String validateUser(UserBean user) {
        String role = "invalid";
        String sql = "SELECT role FROM srs_tbl_user WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                role = rs.getString("role"); // "admin" or "customer"
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    // ---------------- Get UserId by Username ----------------
    @Override
    public int getUserIdByUsername(String username) {
        int userId = -1;
        String sql = "SELECT userId FROM srs_tbl_user WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("userId");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
}
