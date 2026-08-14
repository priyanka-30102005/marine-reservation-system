package com.marine.view;

import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerView {

    public static void showCustomerMenu(Connection conn, int userId, String customerName) {
        boolean exit = false;
        while (!exit) {
            String menu = "Welcome Customer: " + customerName +
                          "\n--- Customer Menu ---" +
                          "\n1. Book Ticket" +
                          "\n2. View Bookings" +
                          "\n3. Logout";

            String choice = JOptionPane.showInputDialog(menu);
            if (choice == null) { // Cancel
                exit = true;
                continue;
            }

            switch (choice) {
                case "1":
                    bookTicket(conn, userId);
                    break;
                case "2":
                    viewBookings(conn, userId);
                    break;
                case "3":
                    exit = true;
                    JOptionPane.showMessageDialog(null, "Logged out.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice! Try again.");
            }
        }
    }
    private static void bookTicket(Connection conn, int userId) {
        try {
            // Fetch available schedules
            String sql = "SELECT s.scheduleId, s.journeyDate, " +
                         "r.source, r.destination, r.travelDuration " +
                         "FROM srs_tbl_schedule s " +
                         "JOIN srs_tbl_route r ON s.routeId = r.routeId";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Integer> scheduleIds = new ArrayList<>();
            StringBuilder sb = new StringBuilder("Available Schedules:\n");
            while (rs.next()) {
                int scheduleId = rs.getInt("scheduleId");
                scheduleIds.add(scheduleId);
                sb.append(scheduleId).append(". ")
                  .append(rs.getString("source")).append(" -> ")
                  .append(rs.getString("destination")).append(", ")
                  .append("Journey Date: ").append(rs.getDate("journeyDate")).append(", ")
                  .append("Duration: ").append(rs.getString("travelDuration")).append("\n");
            }

            if (scheduleIds.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No schedules available!");
                return;
            }

            // Get schedule selection from user
            String input = JOptionPane.showInputDialog(sb.toString() + "\nEnter Schedule ID to book:");
            if (input == null || input.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Booking cancelled!");
                return;
            }

            int scheduleId = Integer.parseInt(input);
            if (!scheduleIds.contains(scheduleId)) {
                JOptionPane.showMessageDialog(null, "Invalid Schedule ID!");
                return;
            }

            // Get number of seats
            String seatsInput = JOptionPane.showInputDialog("Enter number of seats to book:");
            int seats = Integer.parseInt(seatsInput);

            // Generate reservationId manually
            Statement idStmt = conn.createStatement();
            ResultSet idRs = idStmt.executeQuery("SELECT MAX(reservationId) FROM srs_tbl_reservation");
            int reservationId = 1;
            if (idRs.next() && idRs.getInt(1) != 0) {
                reservationId = idRs.getInt(1) + 1;
            }

            // Insert reservation
            String insertSql = "INSERT INTO srs_tbl_reservation (reservationId, userId, scheduleId, noOfSeats) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setInt(1, reservationId);
            ps.setInt(2, userId);
            ps.setInt(3, scheduleId);
            ps.setInt(4, seats);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Ticket booked successfully! Your reservation ID: " + reservationId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void viewBookings(Connection conn, int userId) {
        try {
            // Corrected column names
            String sql = "SELECT r.reservationId, r.noOfSeats, s.journeyDate, " +
                         "rt.source, rt.destination, rt.travelDuration " +
                         "FROM srs_tbl_reservation r " +
                         "JOIN srs_tbl_schedule s ON r.scheduleId = s.scheduleId " +
                         "JOIN srs_tbl_route rt ON s.routeId = rt.routeId " +
                         "WHERE r.userId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Reservation ID: ").append(rs.getInt("reservationId")).append("\n")
                  .append("Route: ").append(rs.getString("source")).append(" -> ").append(rs.getString("destination")).append("\n")
                  .append("Journey Date: ").append(rs.getDate("journeyDate")).append("\n")
                  .append("Seats Booked: ").append(rs.getInt("noOfSeats")).append("\n")
                  .append("Duration: ").append(rs.getString("travelDuration")).append("\n\n");
            }

            if (sb.length() == 0) {
                JOptionPane.showMessageDialog(null, "No bookings found.");
            } else {
                JOptionPane.showMessageDialog(null, "Your Bookings:\n" + sb.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
