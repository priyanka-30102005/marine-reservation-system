package com.marine.bean;

public class ShipBean {
    private int shipId;
    private String shipName;
    private int capacity;

    // Getters & Setters
    public int getShipId() { return shipId; }
    public void setShipId(int shipId) { this.shipId = shipId; }

    public String getShipName() { return shipName; }
    public void setShipName(String shipName) { this.shipName = shipName; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getCapacity() {
        return capacity;
    }

}
