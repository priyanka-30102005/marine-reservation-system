package com.marine.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.marine.bean.ShipBean;

public class ShipDaoImpl implements ShipDao {

    private final String URL = "jdbc:mysql://localhost:3306/srs";
    private final String USER = "root";   // change if needed
    private final String PASSWORD = "root"; // change to your MySQL password
    @Override
    public boolean addShip(ShipBean ship) {
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Include shipId manually
            String sql = "INSERT INTO SRS_TBL_Ship (shipId, shipName, capacity) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ship.getShipId());          // you must set this in ShipBean
            ps.setString(2, ship.getShipName());
            ps.setInt(3, ship.getCapacity());

            int rows = ps.executeUpdate();
            result = (rows > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean updateShip(ShipBean ship) {
        boolean result = false;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE SRS_TBL_Ship SET shipName=?, capacity=? WHERE shipId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ship.getShipName());
            ps.setInt(2, ship.getCapacity());
           // ps.setInt(3, ship.getReservationCapacity());
            ps.setInt(3, ship.getShipId());

            int rows = ps.executeUpdate();
            result = (rows > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteShip(int shipId) {
        boolean result = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 1️⃣ Check if any schedules reference this ship
            String checkSql = "SELECT COUNT(*) FROM SRS_TBL_Schedule WHERE shipId = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, shipId);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // There are schedules assigned to this ship
                    System.out.println("Cannot delete ship! There are schedules assigned to this ship.");
                    return false;
                }
            }

            // 2️⃣ No schedules found, safe to delete
            String deleteSql = "DELETE FROM SRS_TBL_Ship WHERE shipId=?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, shipId);
                int rows = ps.executeUpdate();
                result = (rows > 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // (Optional) Get ship by ID (helpful for checking before update/delete)
    public ShipBean getShipById(int shipId) {
        ShipBean ship = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM SRS_TBL_Ship WHERE shipId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, shipId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ship = new ShipBean();
                ship.setShipId(rs.getInt("shipId"));
                ship.setShipName(rs.getString("shipName"));
                ship.setCapacity(rs.getInt("Capacity"));
                //ship.setReservationCapacity(rs.getInt("reservationCapacity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ship;
    }
    @Override
    public List<ShipBean> getAllShips() {
        List<ShipBean> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM SRS_TBL_Ship";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ShipBean ship = new ShipBean();
                ship.setShipId(rs.getInt("shipId"));
                ship.setShipName(rs.getString("shipName"));
                ship.setCapacity(rs.getInt("Capacity"));
                //ship.setReservationCapacity(rs.getInt("reservationCapacity"));
                list.add(ship);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
