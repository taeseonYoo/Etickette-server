package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.global.model.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - CancelBookingService")
class CancelBookingServiceTest {

    @InjectMocks
    private CancelBookingService cancelBookingService;
    private final BookingRepository bookingRepository = mock(BookingRepository.class);

    @BeforeEach
    void setUp() {
        cancelBookingService = new CancelBookingService(bookingRepository);
    }

    @Test
    @DisplayName("cancelAll - 취소에 성공하면, 상태는 CANCELED_BOOKING 으로 변경된다.")
    void 전체취소_성공() {
        //given
        List<Booking> bookings = List.of(new Booking(1L, 1L, List.of(new Seat("A", 1))), new Booking(1L, 1L, List.of(new Seat("B", 1))));
        BDDMockito.given(bookingRepository.findBySessionId(any())).willReturn(bookings);

        //when
        cancelBookingService.cancelAll(1L);

        //then
        Assertions.assertThat(bookings)
                .extracting(Booking::getStatus)
                .allMatch(status -> "CANCELED_BOOKING".equals(status.getValue()));
    }

    @Test
    @DisplayName("cancel - 개별취소에 성공한다.")
    void 개별취소_성공() {
        //given
        Booking mockBooking = mock(Booking.class);

        BDDMockito.given(bookingRepository.findById(any()))
                .willReturn(Optional.of(mockBooking));

        //when
        cancelBookingService.cancel(any());

        //then
        BDDMockito.verify(mockBooking).cancel();
    }

    @Test
    @DisplayName("cancel - 예약이 존재하지 않으면 ,개별취소에 실패한다.")
    void 개별취소_실패_예약이없음() {
        //given
        BDDMockito.given(bookingRepository.findById(any()))
                .willReturn(Optional.empty());

        //when & then
        assertThrows(BookingNotFoundException.class, () ->
                cancelBookingService.cancel(any()));
    }
}