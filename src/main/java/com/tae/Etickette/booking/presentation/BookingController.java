package com.tae.Etickette.booking.presentation;

import com.tae.Etickette.booking.command.application.BookingService;
import com.tae.Etickette.booking.command.application.CancelBookingService;
import com.tae.Etickette.booking.command.application.dto.BookingRequest;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.query.BookingSummary;
import com.tae.Etickette.booking.query.application.BookingQueryService;
import com.tae.Etickette.booking.query.application.PaymentInfo;
import com.tae.Etickette.global.api.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Booking API", description = "예약 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CancelBookingService cancelBookingService;
    private final BookingQueryService bookingQueryService;

    @Operation(summary = "예매 내역 조회", description = "예매 상태별 조회 or 모든 예매 내역 조회")
    @GetMapping
    public ResponseEntity<SuccessResponse<List<BookingSummary>>> getTicket(@RequestParam(required = false) String status) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(bookingQueryService.getBookingList(email, status)));
    }

    @Operation(summary = "선택 좌석 예약", description = "예매 공연 스케줄Id 및 선택 좌석을 입력받아 예매한다.")
    @PostMapping
    public ResponseEntity<SuccessResponse<String>> booking(@RequestBody BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        BookingRef booking = bookingService.booking(request, email);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.success(booking.getValue()));
    }

    @Operation(summary = "예약 취소", description = "예매 번호를 입력받아 예매를 취소한다.")
    @PatchMapping("/{bookingRef}/cancel")
    public ResponseEntity<SuccessResponse<Void>> cancel(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        cancelBookingService.cancel(new BookingRef(bookingRef), email);
        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.success(null));
    }
    @Operation(summary = "결제 정보 요청",description = "예매 번호를 입력받아, 결제 정보를 반환한다.")
    @GetMapping("/{bookingRef}")
    public ResponseEntity<SuccessResponse<PaymentInfo>> getPaymentInfo(@PathVariable("bookingRef") String bookingRef) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(bookingQueryService.getPaymentInfo(new BookingRef(bookingRef), email)));
    }
}
