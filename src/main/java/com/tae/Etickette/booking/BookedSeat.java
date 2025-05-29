package com.tae.Etickette.booking;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookedSeat {
    private Seat seat;
    private Money price;

    public BookedSeat(Seat seat, Money price) {
        this.seat = seat;
        this.price = price;
    }
}
