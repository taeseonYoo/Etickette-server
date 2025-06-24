package com.tae.Etickette.booking;

import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.booking.application.CancelBookingService;
import com.tae.Etickette.booking.application.dto.BookingRequest;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.booking.query.BookingQueryService;
import com.tae.Etickette.booking.query.PaymentInfo;
import com.tae.Etickette.global.auth.CustomUserDetails;
import com.tae.Etickette.global.model.Canceller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final CancelBookingService cancelBookingService;
    private final BookingQueryService bookingQueryService;

    @PostMapping
    public ResponseEntity<Void> booking(@RequestBody BookingRequest request) {
        bookingService.booking(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{bookingRef}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("bookingRef") String bookingRef) {
        CustomUserDetails details = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        cancelBookingService.cancel(new BookingRef(bookingRef), new Canceller(details.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{bookingRef}")
    public ResponseEntity<PaymentInfo> getPaymentInfo(@PathVariable("bookingRef") String bookingRef) {
        return ResponseEntity.ok(bookingQueryService.getPaymentInfo(new BookingRef(bookingRef)));
    }
}
