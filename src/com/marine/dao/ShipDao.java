package com.marine.dao;

import java.util.List;

import com.marine.bean.ShipBean;

public interface ShipDao {
    boolean addShip(ShipBean ship);
    boolean updateShip(ShipBean ship);   // new
    boolean deleteShip(int shipId);      // new
	List<ShipBean> getAllShips();
}
