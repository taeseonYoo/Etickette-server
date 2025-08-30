package com.tae.Etickette.booking.presentation;

import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.application.CancelBookingService;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.query.BookingSummary;
import com.tae.Etickette.booking.query.application.BookingQueryService;
import com.tae.Etickette.booking.query.application.PaymentInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CancelBookingService cancelBookingService;
    private final BookingQueryService bookingQueryService;

    /**
     * 회원 별 예매 내역 조회
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<BookingSummary>> getTicket() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(bookingQueryService.getBookingList(email));
    }

    @PostMapping
    public ResponseEntity<String> booking(@RequestBody BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        BookingRef booking = bookingService.booking(request, email);
        return ResponseEntity.ok(booking.getValue());
    }

    @PatchMapping("/{bookingRef}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        cancelBookingService.cancel(new BookingRef(bookingRef), email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 결제를 위한 예매 내역 정보 제공
     * @param bookingRef
     * @return
     */
    @GetMapping("/{bookingRef}")
    public ResponseEntity<PaymentInfo> getPaymentInfo(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(bookingQueryService.getPaymentInfo(new BookingRef(bookingRef),email));
    }
}
