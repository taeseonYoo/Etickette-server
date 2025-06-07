package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CancelBookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    public void cancel(BookingRef bookingRef) {
        Booking booking = bookingRepository.findById(bookingRef)
                .orElseThrow(() -> new BookingNotFoundException("예약 내역을 찾을 수 없습니다."));

        booking.cancel();
    }
    @Transactional
    public void cancelAll(Long sessionId) {
        List<Booking> bookings = bookingRepository.findBySessionId(sessionId);

        for (Booking booking : bookings) {
            booking.cancel();
        }
    }

}
