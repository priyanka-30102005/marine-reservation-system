package com.marine.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.marine.bean.ShipBean;
import com.marine.dao.ShipDaoImpl;
import com.marine.bean.RouteBean;
import com.marine.dao.RouteDaoImpl;
import com.marine.bean.ScheduleBean;
import com.marine.dao.ScheduleDaoImpl;

public class AdminView {

    public static void showAdminMenu(String adminName) {
        boolean exit = false;

        while (!exit) {
            String menu = "Welcome Admin: " + adminName +
                          "\n--- Admin Menu ---" +
                          "\n1. Add Ship" +
                          "\n2. Add Route" +
                          "\n3. Add Schedule" +
                          "\n4. Update Ship" +
                          "\n5. Update Route" +
                          "\n6. Update Schedule" +
                          "\n7. Delete Ship" +
                          "\n8. Delete Route" +
                          "\n9. Delete Schedule" +
                          "\n10. View Ships" +
                          "\n11. View Routes" +
                          "\n12. View Schedules" +
                          "\n13. View Reservations" +
                          "\n14. Logout";
                          

            String choice = JOptionPane.showInputDialog(menu);

            if (choice == null) {
                exit = true;
                continue;
            }

            switch (choice) {
                case "1": addShip(); break;
                case "2": addRoute(); break;
                case "3": addSchedule(); break;
                case "4": updateShip(); break;
                case "5": updateRoute(); break;
                case "6": updateSchedule(); break;
                case "7": deleteShip(); break;
                case "8": deleteRoute(); break;
                case "9": deleteSchedule(); break;
                case "10": viewShips(); break;
                case "11": viewRoutes(); break;
                case "12": viewSchedules(); break;
                case "13": viewReservations(); break;
                case "14":
                    exit = true;
                    JOptionPane.showMessageDialog(null, "Logged out.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice! Try again.");
            }
        }
    }

    // ---------------- ADD METHODS ----------------
    private static void addShip() {
        try {
            String name = JOptionPane.showInputDialog("Enter Ship Name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ship Name cannot be empty.");
                return;
            }

            int seating = readInt("Enter Seating Capacity:");
            //int reservation = readInt("Enter Reservation Capacity:");
            int id=readInt("enter id");
            ShipBean ship = new ShipBean();
            ship.setShipName(name.trim());
            ship.setCapacity(seating);
            ship.setShipId(id);
           // ship.setReservationCapacity(reservation);

            ShipDaoImpl dao = new ShipDaoImpl();
            boolean inserted = dao.addShip(ship);

            JOptionPane.showMessageDialog(null, inserted ? "Ship Added Successfully!" : "Failed to Add Ship.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void addRoute() {
        try {
        	 String rid = JOptionPane.showInputDialog("Enter route id:");
             if (rid== null || rid.trim().isEmpty()) return;

            String source = JOptionPane.showInputDialog("Enter Source Port:");
            if (source == null || source.trim().isEmpty()) return;

            String destination = JOptionPane.showInputDialog("Enter Destination Port:");
            if (destination == null || destination.trim().isEmpty()) return;

            String durationStr = JOptionPane.showInputDialog("Enter Travel Duration in days (e.g., 7):");
            if (durationStr == null || durationStr.trim().isEmpty()) return;

            int duration = Integer.parseInt(durationStr.trim()); // convert to int
            int roid = Integer.parseInt(rid.trim()); 
            RouteBean route = new RouteBean();
            route.setSource(source.trim());
            route.setDestination(destination.trim());
            route.setTravelDuration(duration);  // use integer
            route.setRouteId(roid);
            RouteDaoImpl dao = new RouteDaoImpl();
            boolean inserted = dao.addRoute(route);

            JOptionPane.showMessageDialog(null, inserted ? "Route Added Successfully!" : "Failed to Add Route.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid duration! Enter a number in days.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void addSchedule() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/srs", "root", "root")) {

            // 1. Read scheduleId
            String scheduleIdStr = JOptionPane.showInputDialog("Enter Schedule ID:");
            if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) return;
            int scheduleId = Integer.parseInt(scheduleIdStr.trim());

            // 2. Select valid shipId from existing ships
            Statement stmt = conn.createStatement();
            ResultSet rsShips = stmt.executeQuery("SELECT shipId, shipName FROM srs_tbl_ship");
            List<Integer> validShipIds = new ArrayList<>();
            StringBuilder shipList = new StringBuilder("Available Ships:\n");
            while (rsShips.next()) {
                int id = rsShips.getInt("shipId");
                validShipIds.add(id);
                shipList.append(id).append(": ").append(rsShips.getString("shipName")).append("\n");
            }
            if (validShipIds.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No ships available. Add ships first!");
                return;
            }
            String shipIdStr = JOptionPane.showInputDialog(shipList.toString() + "\nEnter Ship ID from list:");
            if (shipIdStr == null || shipIdStr.trim().isEmpty()) return;
            int shipId = Integer.parseInt(shipIdStr.trim());
            if (!validShipIds.contains(shipId)) {
                JOptionPane.showMessageDialog(null, "Invalid Ship ID!");
                return;
            }

            // 3. Select valid routeId from existing routes
            ResultSet rsRoutes = stmt.executeQuery("SELECT routeId, source, destination FROM srs_tbl_route");
            List<Integer> validRouteIds = new ArrayList<>();
            StringBuilder routeList = new StringBuilder("Available Routes:\n");
            while (rsRoutes.next()) {
                int id = rsRoutes.getInt("routeId");
                validRouteIds.add(id);
                routeList.append(id).append(": ").append(rsRoutes.getString("source"))
                         .append(" -> ").append(rsRoutes.getString("destination")).append("\n");
            }
            if (validRouteIds.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No routes available. Add routes first!");
                return;
            }
            String routeIdStr = JOptionPane.showInputDialog(routeList.toString() + "\nEnter Route ID from list:");
            if (routeIdStr == null || routeIdStr.trim().isEmpty()) return;
            int routeId = Integer.parseInt(routeIdStr.trim());
            if (!validRouteIds.contains(routeId)) {
                JOptionPane.showMessageDialog(null, "Invalid Route ID!");
                return;
            }

            // 4. Read journey date
            String dateStr = JOptionPane.showInputDialog("Enter Journey Date (yyyy-mm-dd):");
            if (dateStr == null || dateStr.trim().isEmpty()) return;
            LocalDate journeyDate;
            try {
                journeyDate = LocalDate.parse(dateStr.trim());
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid date format! Use yyyy-mm-dd.");
                return;
            }

            // 5. Create ScheduleBean and insert
            ScheduleBean schedule = new ScheduleBean();
            schedule.setScheduleId(scheduleId);
            schedule.setShipId(shipId);
            schedule.setRouteId(routeId);
            schedule.setJourneyDate(journeyDate);

            ScheduleDaoImpl dao = new ScheduleDaoImpl();
            boolean inserted = dao.addSchedule(schedule);

            JOptionPane.showMessageDialog(null, inserted ? "Schedule Added Successfully!" : "Failed to Add Schedule.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for IDs.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateShip() {
        try {
            int shipId = readInt("Enter Ship ID to Update:");
            String newName = JOptionPane.showInputDialog("Enter New Ship Name:");
            int newSeating = readInt("Enter New Seating Capacity:");
           // int newReservation = readInt("Enter New Reservation Capacity:");

            ShipBean ship = new ShipBean();
            ship.setShipId(shipId);
            ship.setShipName(newName);
            ship.setCapacity(newSeating);
            //ship.setReservationCapacity(newReservation);

            ShipDaoImpl dao = new ShipDaoImpl();
            boolean updated = dao.updateShip(ship);

            JOptionPane.showMessageDialog(null, updated ? "Ship Updated Successfully!" : "Failed to Update Ship.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void updateRoute() {
        try {
            int routeId = readInt("Enter Route ID to Update:");

            String newSource = JOptionPane.showInputDialog("Enter New Source Port:");
            if (newSource == null || newSource.trim().isEmpty()) return;

            String newDestination = JOptionPane.showInputDialog("Enter New Destination Port:");
            if (newDestination == null || newDestination.trim().isEmpty()) return;

            String durationStr = JOptionPane.showInputDialog("Enter New Travel Duration (in days):");
            if (durationStr == null || durationStr.trim().isEmpty()) return;

            int newDuration;
            try {
                newDuration = Integer.parseInt(durationStr.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for duration!");
                return;
            }

            RouteBean route = new RouteBean();
            route.setRouteId(routeId);
            route.setSource(newSource.trim());
            route.setDestination(newDestination.trim());
            route.setTravelDuration(newDuration);  // use int, not Integer/String

            RouteDaoImpl dao = new RouteDaoImpl();
            boolean updated = dao.updateRoute(route);

            JOptionPane.showMessageDialog(null, updated ? "Route Updated Successfully!" : "Failed to Update Route.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateSchedule() {
        try {
            // 1. Read scheduleId
            int scheduleId = readInt("Enter Schedule ID to Update:");

            // 2. Read new shipId and validate it exists
            int shipId = readInt("Enter New Ship ID:");
            ShipDaoImpl shipDao = new ShipDaoImpl();
            ShipBean ship = shipDao.getShipById(shipId);
            if (ship == null) {
                JOptionPane.showMessageDialog(null, "Ship ID not found!");
                return;
            }

            // 3. Read new routeId and validate it exists
            int routeId = readInt("Enter New Route ID:");
            RouteDaoImpl routeDao = new RouteDaoImpl();
            RouteBean route = routeDao.getRouteById(routeId);
            if (route == null) {
                JOptionPane.showMessageDialog(null, "Route not found!");
                return;
            }

            // 4. Read new journey date
            String dateStr = JOptionPane.showInputDialog("Enter New Journey Date (YYYY-MM-DD):");
            if (dateStr == null || dateStr.trim().isEmpty()) return;
            java.time.LocalDate journeyDate;
            try {
                journeyDate = java.time.LocalDate.parse(dateStr.trim());
            } catch (java.time.format.DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid date format! Use YYYY-MM-DD.");
                return;
            }

            // Optional: calculate arrival date if needed
            int duration = route.getTravelDuration();  // matches Route table column
            java.time.LocalDate arrivalDate = journeyDate.plusDays(duration); // can use if needed

            // 5. Create ScheduleBean and set data
            ScheduleBean schedule = new ScheduleBean();
            schedule.setScheduleId(scheduleId);
            schedule.setShipId(shipId);
            schedule.setRouteId(routeId);
            schedule.setJourneyDate(journeyDate);

            // 6. Update schedule in DB
            ScheduleDaoImpl dao = new ScheduleDaoImpl();
            boolean updated = dao.updateSchedule(schedule);

            // 7. Show result
            JOptionPane.showMessageDialog(null, updated ? "Schedule Updated Successfully!" : "Failed to Update Schedule.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for IDs.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ---------------- DELETE METHODS ----------------
    private static void deleteShip() {
        try {
            int shipId = readInt("Enter Ship ID to Delete:");
            ShipDaoImpl dao = new ShipDaoImpl();
            boolean deleted = dao.deleteShip(shipId);

            if (deleted) {
                JOptionPane.showMessageDialog(null, "Ship Deleted Successfully!");
            } else {
                // If deleteShip returned false, it could be due to schedules
                ScheduleDaoImpl scheduleDao = new ScheduleDaoImpl();
                List<ScheduleBean> schedules = scheduleDao.getSchedulesByShipId(shipId);
                if (!schedules.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Cannot delete ship! There are schedules assigned to this ship.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete ship. Ship may not exist.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void deleteRoute() {
        try {
            int routeId = readInt("Enter Route ID to Delete:");
            RouteDaoImpl dao = new RouteDaoImpl();

            // Check for schedules using this route
            ScheduleDaoImpl scheduleDao = new ScheduleDaoImpl();
            List<ScheduleBean> schedules = scheduleDao.getSchedulesByRouteId(routeId);

            if (!schedules.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cannot delete route! There are schedules assigned to this route.");
                return;
            }

            // Safe to delete
            boolean deleted = dao.deleteRoute(routeId);
            JOptionPane.showMessageDialog(null, deleted ? "Route Deleted Successfully!" : "Failed to Delete Route.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void deleteSchedule() {
        try {
            int scheduleId = readInt("Enter Schedule ID to Delete:");
            ScheduleDaoImpl dao = new ScheduleDaoImpl();
            boolean deleted = dao.deleteSchedule(scheduleId);
            JOptionPane.showMessageDialog(null, deleted ? "Schedule Deleted Successfully!" : "Failed to Delete Schedule.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // ---------------- Utility ----------------
    private static int readInt(String message) {
        while (true) {
            String input = JOptionPane.showInputDialog(message);
            if (input == null) throw new RuntimeException("Input cancelled by user.");
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid number! Please enter a valid integer.");
            }
        }
    }
    private static void viewShips() {
        ShipDaoImpl dao = new ShipDaoImpl();
        java.util.List<ShipBean> ships = dao.getAllShips();
        if (ships == null || ships.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No ships found.");
            return;
        }
        StringBuilder sb = new StringBuilder("---- Ships ----\n");
        for (ShipBean ship : ships) {
            sb.append("ID: ").append(ship.getShipId())
              .append(", Name: ").append(ship.getShipName())
              .append(", Seating: ").append(ship.getCapacity())
              //.append(", Reservation: ").append(ship.getReservationCapacity())
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void viewRoutes() {
        RouteDaoImpl dao = new RouteDaoImpl();
        java.util.List<RouteBean> routes = dao.getAllRoutes();
        if (routes == null || routes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No routes found.");
            return;
        }
        StringBuilder sb = new StringBuilder("---- Routes ----\n");
        for (RouteBean route : routes) {
            sb.append("ID: ").append(route.getRouteId())
              .append(", Source: ").append(route.getSource())
              .append(", Destination: ").append(route.getDestination())
              .append(", Duration: ").append(route.getTravelDuration())
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void viewSchedules() {
        ScheduleDaoImpl dao = new ScheduleDaoImpl();
        java.util.List<ScheduleBean> schedules = dao.getAllSchedules();
        if (schedules == null || schedules.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No schedules found.");
            return;
        }
        StringBuilder sb = new StringBuilder("---- Schedules ----\n");
        for (ScheduleBean schedule : schedules) {
            sb.append("ID: ").append(schedule.getScheduleId())
              .append(", Ship ID: ").append(schedule.getShipId())
              .append(", Route ID: ").append(schedule.getRouteId())
              .append(", Departure: ").append(schedule.getJourneyDate())
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void viewReservations() {
        com.marine.dao.ReservationDaoImpl dao = new com.marine.dao.ReservationDaoImpl();
        java.util.List<com.marine.bean.ReservationBean> reservations = dao.getAllReservations();
        if (reservations == null || reservations.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No reservations found.");
            return;
        }
        StringBuilder sb = new StringBuilder("---- Reservations ----\n");
        for (com.marine.bean.ReservationBean r : reservations) {
            sb.append("ID: ").append(r.getReservationId())
              .append(", User: ").append(r.getUserId())
              .append(", Schedule: ").append(r.getScheduleId())
              .append(", Seats Booked: ").append(r.getNoOfSeats())  // <- use getNoOfSeats()
              .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

}