package com.tae.Etickette.booking.presentation;

import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.application.CancelBookingService;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.query.BookingQueryService;
import com.tae.Etickette.booking.query.PaymentInfo;
import com.tae.Etickette.global.auth.CustomUserDetails;
import com.tae.Etickette.global.model.Canceller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final CancelBookingService cancelBookingService;
    private final BookingQueryService bookingQueryService;

    @PostMapping
    public ResponseEntity<String> booking(@RequestBody BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        BookingRef booking = bookingService.booking(request, email);
        return ResponseEntity.ok(booking.getValue());
    }

    @PostMapping("/{bookingRef}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        cancelBookingService.cancel(new BookingRef(bookingRef), email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{bookingRef}")
    public ResponseEntity<PaymentInfo> getPaymentInfo(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(bookingQueryService.getPaymentInfo(new BookingRef(bookingRef),email));
    }
}
