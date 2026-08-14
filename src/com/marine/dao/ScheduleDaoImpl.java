package com.marine.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.marine.bean.ScheduleBean;

public class ScheduleDaoImpl implements ScheduleDao {

    private final String URL = "jdbc:mysql://localhost:3306/srs";
    private final String USER = "root";
    private final String PASSWORD = "root";

    public boolean addSchedule(ScheduleBean schedule) {
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO SRS_TBL_Schedule (scheduleId, shipId, routeId, journeyDate) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, schedule.getScheduleId());   // manually provide scheduleId
            ps.setInt(2, schedule.getShipId());
            ps.setInt(3, schedule.getRouteId());
            ps.setDate(4, java.sql.Date.valueOf(schedule.getJourneyDate())); // convert LocalDate to java.sql.Date

            int rows = ps.executeUpdate();
            result = (rows > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ScheduleBean> getSchedulesByShipId(int shipId) {
        List<ScheduleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM SRS_TBL_Schedule WHERE shipId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shipId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ScheduleBean schedule = new ScheduleBean();
                schedule.setScheduleId(rs.getInt("scheduleId"));
                schedule.setShipId(rs.getInt("shipId"));
                schedule.setRouteId(rs.getInt("routeId"));
                schedule.setJourneyDate(rs.getDate("journeyDate").toLocalDate());
                list.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }



    @Override
    public boolean updateSchedule(ScheduleBean schedule) {
        boolean result = false;
        String sql = "UPDATE SRS_TBL_Schedule SET shipId = ?, routeId = ?, journeyDate = ? WHERE scheduleId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, schedule.getShipId());
            ps.setInt(2, schedule.getRouteId());

            // If journeyDate is LocalDate
            ps.setDate(3, java.sql.Date.valueOf(schedule.getJourneyDate()));

            ps.setInt(4, schedule.getScheduleId());

            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean deleteSchedule(int scheduleId) {
        boolean result = false;
        String sql = "DELETE FROM SRS_TBL_Schedule WHERE scheduleId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<ScheduleBean> getAllSchedules() {
        List<ScheduleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM SRS_TBL_Schedule";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ScheduleBean schedule = new ScheduleBean();
                schedule.setScheduleId(rs.getInt("scheduleId"));
                schedule.setShipId(rs.getInt("shipId"));
                schedule.setRouteId(rs.getInt("routeId"));
                // if your ScheduleBean has journeyDate as String:
                //schedule.setJourneyDate(rs.getDate("journeyDate").toString());
                // or if you store it as Date in bean:
                schedule.setJourneyDate(rs.getDate("journeyDate").toLocalDate());
                list.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ScheduleBean> getSchedulesByRouteId(int routeId) {
        List<ScheduleBean> list = new ArrayList<>();
        String sql = "SELECT * FROM SRS_TBL_Schedule WHERE routeId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ScheduleBean schedule = new ScheduleBean();
                schedule.setScheduleId(rs.getInt("scheduleId"));
                schedule.setShipId(rs.getInt("shipId"));
                schedule.setRouteId(rs.getInt("routeId"));
                schedule.setJourneyDate(rs.getDate("journeyDate").toLocalDate());
                list.add(schedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

	
	

}
