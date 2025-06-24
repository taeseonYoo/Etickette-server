package com.tae.Etickette.booking.query;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.seat.query.SeatData;
import com.tae.Etickette.seat.query.SeatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingQueryService {
    private final BookingRepository bookingRepository;
    private final SeatQueryService seatQueryService;

    public PaymentInfo getPaymentInfo(BookingRef bookingRef) {
        Booking booking = bookingRepository.findById(bookingRef)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 예약 번호입니다."));

        List<LineItemInfo> lineItems = booking.getLineItems().stream()
                .map(lineItem -> {
                    SeatData seatData = seatQueryService.getSeat(lineItem.getSeatId().getSeatId())
                            .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));
                    return new LineItemInfo(lineItem, seatData);
                }).toList();

        return new PaymentInfo(booking, lineItems);
    }
}
