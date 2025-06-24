package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.global.event.Event;
import lombok.Getter;

import java.util.List;

@Getter
public class ConfirmPaymentEvent extends Event {
    private List<BookSeatId> bookSeatIds;

    public ConfirmPaymentEvent(List<BookSeatId> bookSeatIds) {
        this.bookSeatIds = bookSeatIds;
    }
}
