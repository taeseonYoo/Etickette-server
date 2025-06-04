package com.tae.Etickette.session.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    //예매 번호
    @EmbeddedId
    private BookingRef bookingRef;
    private Long memberId;

    @ElementCollection
    @CollectionTable(name = "booked_seat",joinColumns = @JoinColumn(name = "booking_id"))
    private List<Seat> seats = new ArrayList<>();

    private final int MAX_SEATS_ALLOWED_PER_BOOKING = 2;

    public Booking(BookingRef bookingRef, Long memberId, List<Seat> seats) {
        this.bookingRef = bookingRef;
        this.memberId = memberId;
        verifySeatsAllowedPerBooking(seats);
        this.seats = seats;
    }

    private void verifySeatsAllowedPerBooking(List<Seat> seats) {
        if (seats.isEmpty()) throw new RuntimeException();
        else if(maxSeatsAllowedPerBooking(seats)) throw new RuntimeException();
    }

    private boolean maxSeatsAllowedPerBooking(List<Seat> seats) {
        return seats.size() > MAX_SEATS_ALLOWED_PER_BOOKING;
    }
}
