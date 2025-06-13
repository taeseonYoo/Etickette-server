package com.tae.Etickette.booking.domain;

import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.global.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public class BookingCanceledEvent extends Event {
    private List<BookSeatId> bookedSeatIds;
    private Long memberId;
    private BookingRef bookingRef;

    public BookingCanceledEvent(List<BookSeatId> bookedSeatIds,Long memberId, BookingRef bookingRef) {
        this.bookedSeatIds = bookedSeatIds;
        this.memberId = memberId;
        this.bookingRef = bookingRef;
    }
}
