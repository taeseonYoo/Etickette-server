package com.tae.Etickette.booking.application;

import com.tae.Etickette.common.model.Seat;
import com.tae.Etickette.session.domain.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingPolicy implements BookingPolicy{

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
