package com.tae.Etickette.booking.application;

import com.tae.Etickette.booking.command.application.CancelBookingService;
import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.booking.command.domain.CancelPolicy;
import com.tae.Etickette.booking.command.domain.SeatItem;
import com.tae.Etickette.booking.infra.BookingRepository;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.global.model.Money;
import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - CancelBookingService")
class CancelBookingServiceTest {

    @InjectMocks
    private CancelBookingService cancelBookingService;
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final CancelPolicy cancelPolicy = mock(CancelPolicy.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

    @Test
    @DisplayName("cancelAll - 취소에 성공하면, 상태는 CANCELED_BOOKING 으로 변경된다.")
    void 전체취소_성공() {
        //given
        Booking booking = new Booking(1L, 1L, List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(150000))));
        List<Booking> bookings = List.of(booking);
        BDDMockito.given(bookingRepository.findBySessionId(any())).willReturn(bookings);
        BDDMockito.given(cancelPolicy.hasEntireCancelPermission()).willReturn(true);
        BDDMockito.given(bookingRepository.findById(booking.getBookingRef())).willReturn(Optional.of(booking));
        BDDMockito.doNothing().when(eventPublisher).publishEvent(any());

        //when
        cancelBookingService.cancelAll(1L);

        //then
        Assertions.assertThat(bookings)
                .extracting(Booking::getStatus)
                .allMatch(status -> "CANCELED_BOOKING".equals(status.getValue()));
    }
    @Test
    @DisplayName("cancelAll - 권한이 ADMIN 이 아니면, 전체취소에 실패한다.")
    void 전체취소_실패_권한이없음() {
        //given
        List<Booking> bookings = List.of(new Booking(1L, 1L,
                        List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(150000)))),
                new Booking(1L, 1L,
                        List.of(new SeatItem(new BookSeatId(2L, 1L), new Money(150000)))));
        BDDMockito.given(bookingRepository.findBySessionId(any())).willReturn(bookings);
        BDDMockito.given(cancelPolicy.hasEntireCancelPermission()).willReturn(false);

        //when & then
        assertThrows(ForbiddenException.class, () ->
                cancelBookingService.cancelAll(1L));
    }

    @Test
    @DisplayName("cancel - 개별취소에 성공한다.")
    void 개별취소_성공() {
        //given
        Booking mockBooking = mock(Booking.class);

        BDDMockito.given(bookingRepository.findById(any()))
                .willReturn(Optional.of(mockBooking));
        BDDMockito.given(cancelPolicy.hasCancellationPermission(any(), any()))
                .willReturn(true);
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(mock(Member.class)));

        //when
        cancelBookingService.cancel(any(),"test@test");

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
        assertThrows(ResourceNotFoundException.class, () ->
                cancelBookingService.cancel(any(),"test@test"));
    }

    @Test
    @DisplayName("cancel - 취소 권한이 없다면 ,개별취소에 실패한다.")
    void 개별취소_실패_취소권한이() {
        //given
        BDDMockito.given(bookingRepository.findById(any()))
                .willReturn(Optional.of(mock(Booking.class)));
        BDDMockito.given(cancelPolicy.hasCancellationPermission(any(), any()))
                .willReturn(false);
        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(mock(Member.class)));

        //when & then
        assertThrows(ForbiddenException.class, () ->
                cancelBookingService.cancel(any(),"test@test"));
    }


}