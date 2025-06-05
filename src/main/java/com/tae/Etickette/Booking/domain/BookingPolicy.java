package com.tae.Etickette.Booking.domain;

import com.tae.Etickette.session.domain.Seat;
import com.tae.Etickette.session.domain.Session;

import java.util.List;

public interface BookingPolicy {
    void verifySeatsNotAlreadyBooked(Session session, List<Seat> bookedSeats, List<Seat> requestSeats);
}
