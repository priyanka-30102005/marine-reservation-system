package com.marine.dao;

import com.marine.bean.ReservationBean;
import java.util.List;

public interface ReservationDao {
    boolean bookTicket(ReservationBean reservation);
    List<ReservationBean> viewReservationsByUser(int userId);
    List<ReservationBean> getAllReservations();

}
