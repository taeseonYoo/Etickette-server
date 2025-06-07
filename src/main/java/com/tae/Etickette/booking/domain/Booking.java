package com.tae.Etickette.booking.domain;

import com.tae.Etickette.global.model.Seat;
import com.tae.Etickette.session.domain.AlreadyStartedException;
import com.tae.Etickette.session.domain.SessionStatus;
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
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

//    private Long paymentId;

    @ElementCollection
    @CollectionTable(name = "booked_seat",joinColumns = @JoinColumn(name = "booking_id"))
    private List<Seat> seats = new ArrayList<>();


    public Booking(Long memberId, Long sessionId, List<Seat> seats) {
        this.bookingRef = BookingRef.generate();
        this.memberId = memberId;
        this.sessionId = sessionId;
        verifySeatsAllowedPerBooking(seats);
        this.seats = seats;
        status = BookingStatus.BEFORE_PAY;
    }

    public static Booking create(Long memberId, Long sessionId, List<Seat> seats) {
        return new Booking(memberId, sessionId, seats);
    }

    public void cancel() {
        if (isNotCanceled()) {
            this.status = BookingStatus.CANCELED_BOOKING;
        }
    }

    private boolean isNotCanceled() {
        return status == BookingStatus.COMPLETED_BOOKING || status == BookingStatus.BEFORE_PAY;
    }

    private void verifySeatsAllowedPerBooking(List<Seat> seats) {
        if (seats.isEmpty()) throw new RuntimeException();
        else if(maxSeatsAllowedPerBooking(seats)) throw new RuntimeException();

    }
    private final int MAX_SEATS_ALLOWED_PER_BOOKING = 2;
    private boolean maxSeatsAllowedPerBooking(List<Seat> seats) {
        return seats.size() > MAX_SEATS_ALLOWED_PER_BOOKING;
    }
}
