package com.marine.dao;

import java.util.List;

import com.marine.bean.ScheduleBean;

public interface ScheduleDao {
    boolean addSchedule(ScheduleBean schedule);
    public List<ScheduleBean> getSchedulesByShipId(int shipId);
    boolean updateSchedule(ScheduleBean schedule);
    boolean deleteSchedule(int scheduleId);
	List<ScheduleBean> getAllSchedules();
	public List<ScheduleBean> getSchedulesByRouteId(int routeId);
}

