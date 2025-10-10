package com.tae.Etickette.bookseat.command.application;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.BookingRef;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.bookseat.command.domain.BookSeat;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.command.domain.CancelSeatPolicy;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.global.model.Canceller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - CancelBookSeatService")
class CancelBookSeatServiceTest {
    @InjectMocks
    CancelBookSeatService cancelBookSeatService;
    BookSeatRepository bookSeatRepository = mock(BookSeatRepository.class);
    BookingRepository bookingRepository = mock(BookingRepository.class);
    CancelSeatPolicy cancelSeatPolicy = mock(CancelSeatPolicy.class);

    private BookingRef mockBookingRef;
    private BookSeatId mockBookSeatId;
    private Booking mockBooking;
    private BookSeat mockBookSeat;
    private Canceller mockCanceller;
    @BeforeEach
    void setUp() {
        cancelBookSeatService = new CancelBookSeatService(bookSeatRepository, bookingRepository, cancelSeatPolicy);
        mockBookingRef = new BookingRef("REF1234");
        mockBookSeatId = new BookSeatId(1L, 1L);
        mockBooking = mock(Booking.class);
        mockBookSeat = mock(BookSeat.class);
        mockCanceller = mock(Canceller.class);
    }
    @Test
    @DisplayName("좌석 취소에 성공한다.")
    void cancel_success() {
        //given
        BDDMockito.given(bookingRepository.findById(mockBookingRef))
                .willReturn(Optional.of(mockBooking));
        BDDMockito.given(bookSeatRepository.findById(mockBookSeatId))
                .willReturn(Optional.of(mockBookSeat));
        BDDMockito.given(cancelSeatPolicy.hasCancellationPermission(mockBooking, mockCanceller))
                .willReturn(true);
        //when
        cancelBookSeatService.cancel(mockBookingRef, mockBookSeatId, mockCanceller);
        //then
        verify(mockBookSeat, times(1)).cancel();
    }
    @Test
    @DisplayName("예매 조회에 실패하면, 좌석 취소에 실패한다.")
    void cancel_fail_booking() {
        //given
        BDDMockito.given(bookingRepository.findById(mockBookingRef))
                .willReturn(Optional.empty());
        //when
        Assertions.assertThatThrownBy(() ->
                        cancelBookSeatService.cancel(mockBookingRef, mockBookSeatId, mockCanceller))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKING_NOT_FOUND);
    }
    @Test
    @DisplayName("좌석 조회에 실패하면, 좌석 취소에 실패한다.")
    void cancel_fail_bookSeat() {
        //given
        BDDMockito.given(bookingRepository.findById(mockBookingRef))
                .willReturn(Optional.of(mockBooking));
        BDDMockito.given(bookSeatRepository.findById(mockBookSeatId))
                .willReturn(Optional.empty());
        //when
        Assertions.assertThatThrownBy(() ->
                        cancelBookSeatService.cancel(mockBookingRef, mockBookSeatId, mockCanceller))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_NOT_FOUND);
    }
    @Test
    @DisplayName("좌석 취소 권한이 없으면, 좌석 취소에 실패한다.")
    void cancel_fail_policy() {
        //given
        BDDMockito.given(bookingRepository.findById(mockBookingRef))
                .willReturn(Optional.of(mockBooking));
        BDDMockito.given(bookSeatRepository.findById(mockBookSeatId))
                .willReturn(Optional.of(mockBookSeat));
        BDDMockito.given(cancelSeatPolicy.hasCancellationPermission(mockBooking, mockCanceller))
                .willReturn(false);
        //when
        Assertions.assertThatThrownBy(() ->
                        cancelBookSeatService.cancel(mockBookingRef, mockBookSeatId, mockCanceller))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NO_PERMISSION);
    }
}