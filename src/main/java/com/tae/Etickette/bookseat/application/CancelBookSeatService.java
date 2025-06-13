package com.tae.Etickette.bookseat.application;

import com.tae.Etickette.booking.application.BookingNotFoundException;
import com.tae.Etickette.booking.application.NoCancellablePermission;
import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.bookseat.domain.CancelSeatPolicy;
import com.tae.Etickette.bookseat.domain.BookSeat;
import com.tae.Etickette.bookseat.domain.BookSeatId;
import com.tae.Etickette.bookseat.domain.Canceller;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelBookSeatService {
    private final BookSeatRepository bookSeatRepository;
    private final BookingRepository bookingRepository;

    private final CancelSeatPolicy cancelSeatPolicy;
    @Transactional
    public void cancel(BookingRef bookingRef, BookSeatId bookSeatId, Canceller canceller) {
        //좌석 검증
        Booking booking = bookingRepository.findById(bookingRef)
                .orElseThrow(() -> new BookingNotFoundException("예약을 찾을 수 없습니다."));

        BookSeat bookSeat = bookSeatRepository.findById(bookSeatId)
                .orElseThrow(() -> new RuntimeException("임시"));

        if (!cancelSeatPolicy.hasCancellationPermission(booking, canceller)) {
            throw new NoCancellablePermission();
        }

        bookSeat.cancel();
    }
}
