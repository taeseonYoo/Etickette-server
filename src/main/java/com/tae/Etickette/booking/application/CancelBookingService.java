package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.domain.CancelPolicy;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.global.model.Canceller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelBookingService {

    private final BookingRepository bookingRepository;
    private final CancelPolicy cancelPolicy;

    @Transactional
    public void cancel(BookingRef bookingRef, Canceller canceller) {

        Booking booking = bookingRepository.findById(bookingRef)
                .orElseThrow(() -> new BookingNotFoundException("예약 내역을 찾을 수 없습니다."));

        if (!cancelPolicy.hasCancellationPermission(booking, canceller)) {
            throw new NoCancellablePermission("취소 권한이 없습니다.");
        }

        booking.cancel();
    }
    @Transactional
    public void cancelAll(Long sessionId) {
        List<Booking> bookings = bookingRepository.findBySessionId(sessionId);

        if (!cancelPolicy.hasEntireCancelPermission()) {
            throw new NoCancellablePermission("관리자만 예약을 전체 취소 할 수 있습니다.");
        }

        for (Booking booking : bookings) {
            booking.cancel();
        }
    }

}
