package com.marine.view;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import com.marine.bean.UserBean;
import com.marine.dao.UserDaoImpl;

public class Main {
    public static void main(String[] args) {
        try {
            // DB Connection
            String url = "jdbc:mysql://localhost:3306/srs";
            String user = "root";
            String password = "root";
            Connection conn = DriverManager.getConnection(url, user, password);

            JOptionPane.showMessageDialog(null, "Database Connected Successfully!");

            boolean exit = false;
            UserDaoImpl userDao = new UserDaoImpl();

            while (!exit) {
                String menu = "--- Welcome to SRS System ---\n" +
                              "1. Register\n" +
                              "2. Login\n" +
                              "3. Exit";

                String choice = JOptionPane.showInputDialog(menu);
                if (choice == null) break;

                switch (choice) {
                    case "1": // Register
                        String regUsername = JOptionPane.showInputDialog("Enter new username:");
                        if (regUsername == null || regUsername.trim().isEmpty()) break;

                        String regPassword = JOptionPane.showInputDialog("Enter new password:");
                        if (regPassword == null || regPassword.trim().isEmpty()) break;

                        UserBean newUser = new UserBean();
                        newUser.setUsername(regUsername.trim());
                        newUser.setPassword(regPassword.trim());
                        newUser.setRole("customer"); // force role as customer

                        boolean registered = userDao.registerUser(newUser);
                        if (registered) {
                            JOptionPane.showMessageDialog(null, "Registration successful! Please login.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Registration failed. Try another username.");
                        }
                        break;

                    case "2": // Login
                        String loginUsername = JOptionPane.showInputDialog("Enter username:");
                        if (loginUsername == null || loginUsername.trim().isEmpty()) break;

                        String loginPassword = JOptionPane.showInputDialog("Enter password:");
                        if (loginPassword == null || loginPassword.trim().isEmpty()) break;

                        UserBean ub = new UserBean();
                        ub.setUsername(loginUsername.trim());
                        ub.setPassword(loginPassword.trim());

                        String result = userDao.validateUser(ub); // returns "admin" or "customer"

                        if ("admin".equalsIgnoreCase(result)) {
                            JOptionPane.showMessageDialog(null, "Welcome Admin: " + ub.getUsername());
                            AdminView.showAdminMenu(ub.getUsername());
                        } else if ("customer".equalsIgnoreCase(result)) {
                            int userId = userDao.getUserIdByUsername(ub.getUsername());
                            if (userId != -1) {
                                JOptionPane.showMessageDialog(null, "Welcome Customer: " + ub.getUsername());
                                CustomerView.showCustomerMenu(conn, userId, ub.getUsername());
                            } else {
                                JOptionPane.showMessageDialog(null, "User ID not found.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Credentials! Try again.");
                        }
                        break;

                    case "3": // Exit
                        exit = true;
                        JOptionPane.showMessageDialog(null, "Thank you for using SRS System!");
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Invalid choice! Try again.");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
