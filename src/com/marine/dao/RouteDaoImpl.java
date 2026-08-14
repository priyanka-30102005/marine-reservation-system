package com.marine.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.marine.bean.RouteBean;

public class RouteDaoImpl implements RouteDao {

    private final String URL = "jdbc:mysql://localhost:3306/srs";
    private final String USER = "root";
    private final String PASSWORD = "root";

    @Override
    public boolean addRoute(RouteBean route) {
        boolean result = false;
        // Correct column name: travelDuration
        String sql = "INSERT INTO SRS_TBL_Route (routeId, source, destination, travelDuration) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, route.getRouteId());
            ps.setString(2, route.getSource());
            ps.setString(3, route.getDestination());
            ps.setInt(4, route.getTravelDuration());

            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public RouteBean getRouteById(int routeId) {
        RouteBean route = null;
        String sql = "SELECT * FROM SRS_TBL_Route WHERE routeId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                route = new RouteBean();
                route.setRouteId(rs.getInt("routeId"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                route.setTravelDuration(rs.getInt("travelDuration"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return route;
    }

    @Override
    public boolean updateRoute(RouteBean route) {
        boolean result = false;
        String sql = "UPDATE SRS_TBL_Route SET source = ?, destination = ?, travelDuration = ? WHERE routeId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, route.getSource());
            ps.setString(2, route.getDestination());
            ps.setInt(3, route.getTravelDuration());
            ps.setInt(4, route.getRouteId());

            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteRoute(int routeId) {
        boolean result = false;
        String sql = "DELETE FROM SRS_TBL_Route WHERE routeId = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, routeId);
            int rows = ps.executeUpdate();
            result = (rows > 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<RouteBean> getAllRoutes() {
        List<RouteBean> list = new ArrayList<>();
        String sql = "SELECT * FROM SRS_TBL_Route";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RouteBean route = new RouteBean();
                route.setRouteId(rs.getInt("routeId"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                //route.setFare(rs.getDouble("fare")); // if you rename your bean field
                list.add(route);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
