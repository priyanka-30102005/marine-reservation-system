package com.marine.bean;
import java.time.LocalDate;

public class ScheduleBean {
    private int scheduleId;   // must set manually or make AUTO_INCREMENT
    private int shipId;
    private int routeId;
    private LocalDate journeyDate;  // use java.time.LocalDate

    // Getters and setters
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getShipId() { return shipId; }
    public void setShipId(int shipId) { this.shipId = shipId; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public LocalDate getJourneyDate() { return journeyDate; }
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
}
