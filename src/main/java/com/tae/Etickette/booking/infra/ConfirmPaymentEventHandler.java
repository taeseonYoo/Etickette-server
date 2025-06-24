package com.tae.Etickette.booking.infra;

import com.tae.Etickette.booking.command.domain.ConfirmPaymentEvent;
import com.tae.Etickette.bookseat.application.BookSeatService;
import com.tae.Etickette.bookseat.domain.BookSeatId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentEventHandler {
    private final BookSeatService bookSeatService;

    @EventListener(ConfirmPaymentEvent.class)
    public void handle(ConfirmPaymentEvent event) {
        List<BookSeatId> bookSeatIds = event.getBookSeatIds();

        for (BookSeatId bookSeatId : bookSeatIds) {
            bookSeatService.book(bookSeatId);
        }
    }
}
