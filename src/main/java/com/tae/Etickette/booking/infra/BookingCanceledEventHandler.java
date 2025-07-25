package com.tae.Etickette.booking.infra;

import com.tae.Etickette.booking.command.domain.BookingCanceledEvent;
import com.tae.Etickette.bookseat.command.application.CancelBookSeatService;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.global.model.Canceller;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingCanceledEventHandler {
    private CancelBookSeatService cancelBookSeatService;

    public BookingCanceledEventHandler(CancelBookSeatService cancelBookSeatService) {
        this.cancelBookSeatService = cancelBookSeatService;
    }

    @EventListener(BookingCanceledEvent.class)
    public void handle(BookingCanceledEvent event) {
        //좌석을 취소한다.
        List<BookSeatId> bookedSeatIds = event.getBookedSeatIds();

        for (BookSeatId bookedSeatId : bookedSeatIds) {
            cancelBookSeatService.cancel(event.getBookingRef(), bookedSeatId, new Canceller(event.getMemberId()));
        }
    }
}
