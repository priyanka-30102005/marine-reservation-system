package com.marine.bean;
import java.sql.Date;

public class ReservationBean {
    // 1️⃣ Fields (these must exist)
    private int reservationId;
    private int userId;
    private int scheduleId;
    private int noOfSeats;
    private Date bookingDate;   // java.sql.Date or java.util.Date depending on your import

    // 2️⃣ Constructor
    public ReservationBean(int reservationId, int userId, int scheduleId,
                           int noOfSeats, Date bookingDate) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.noOfSeats = noOfSeats;
        this.bookingDate = bookingDate;
    }

    // 3️⃣ Getters and setters
    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }
    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public Date getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
