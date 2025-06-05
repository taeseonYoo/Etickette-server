package com.tae.Etickette.Booking.domain;

import com.tae.Etickette.session.domain.Seat;
import com.tae.Etickette.session.domain.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultBookingPolicy implements BookingPolicy{

    @Override
    public void verifySeatsNotAlreadyBooked(Session session, List<Seat> bookedSeats, List<Seat> requestSeats) {
        //유효한 좌석인 지 검증
        session.verifySeatsExist(requestSeats);


        for (Seat requestedSeat : requestSeats) {
            if (bookedSeats.contains(requestedSeat)) {
                throw new RuntimeException();
            }
        }
    }
}
