package com.tae.Etickette.Booking.domain;

import com.tae.Etickette.session.domain.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    //예매 번호
    @EmbeddedId
    private BookingRef bookingRef;

    private Long memberId;
    private Long sessionId;

//    private Long paymentId;

    @ElementCollection
    @CollectionTable(name = "booked_seat",joinColumns = @JoinColumn(name = "booking_id"))
    private List<Seat> seats = new ArrayList<>();

    private final int MAX_SEATS_ALLOWED_PER_BOOKING = 2;

    public Booking(Long memberId, Long sessionId, List<Seat> seats) {
        this.memberId = memberId;
        this.sessionId = sessionId;
        verifySeatsAllowedPerBooking(seats);
        this.seats = seats;

    }

    public static Booking create(Long memberId, Long sessionId, List<Seat> seats) {
        return new Booking(memberId, sessionId, seats);
    }


    private void verifySeatsAllowedPerBooking(List<Seat> seats) {
        if (seats.isEmpty()) throw new RuntimeException();
        else if(maxSeatsAllowedPerBooking(seats)) throw new RuntimeException();

    }

    private boolean maxSeatsAllowedPerBooking(List<Seat> seats) {
        return seats.size() > MAX_SEATS_ALLOWED_PER_BOOKING;
    }

}
