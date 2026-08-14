package com.marine.dao;

import java.sql.*;
import java.util.*;
import com.marine.bean.ReservationBean;

public class ReservationDaoImpl implements ReservationDao {
    private static final String URL = "jdbc:mysql://localhost:3306/srs";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    @Override
    public boolean bookTicket(ReservationBean r) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO SRS_TBL_Reservation(reservationId, userId, scheduleId, noOfSeats, bookingDate) VALUES (?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, r.getReservationId());
            ps.setInt(2, r.getUserId());
            ps.setInt(3, r.getScheduleId());
            ps.setInt(4, r.getNoOfSeats());
            ps.setDate(5, r.getBookingDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<ReservationBean> viewReservationsByUser(int userId) {
        List<ReservationBean> list = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM SRS_TBL_Reservation WHERE userId=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ReservationBean(
                    rs.getInt("reservationId"),
                    rs.getInt("userId"),
                    rs.getInt("scheduleId"),
                    rs.getInt("noOfSeats"),
                     rs.getDate("bookingDate")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    @Override
    public List<ReservationBean> getAllReservations() {
        List<ReservationBean> list = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM SRS_TBL_Reservation";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReservationBean r = new ReservationBean(
                    rs.getInt("reservationId"),
                    rs.getInt("userId"),
                    rs.getInt("scheduleId"),
                    rs.getInt("noOfSeats"),
                    rs.getDate("bookingDate")
                );
                list.add(r);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

	
}
