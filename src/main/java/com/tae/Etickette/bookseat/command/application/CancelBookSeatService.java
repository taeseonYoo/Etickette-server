package com.tae.Etickette.bookseat.command.application;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.bookseat.command.domain.CancelSeatPolicy;
import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.global.model.Canceller;
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKING_NOT_FOUND, "예약을 찾을 수 없습니다."));

        BookSeat bookSeat = bookSeatRepository.findById(bookSeatId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.BOOKSEAT_NOT_FOUND, "예매 좌석을 찾을 수 없습니다."));

        if (!cancelSeatPolicy.hasCancellationPermission(booking, canceller)) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION, "예약 좌석을 취소할 권한이 없습니다.");
        }

        bookSeat.cancel();
    }
}
