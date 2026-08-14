package com.marine.dao;

import java.util.List;

import com.marine.bean.RouteBean;

public interface RouteDao {
    boolean addRoute(RouteBean route);
    RouteBean getRouteById(int routeId);
    boolean updateRoute(RouteBean route);
    boolean deleteRoute(int routeId);
	List<RouteBean> getAllRoutes();
}
