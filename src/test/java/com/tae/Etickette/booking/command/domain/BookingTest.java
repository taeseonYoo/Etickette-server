package com.tae.Etickette.booking.command.domain;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.model.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


class BookingTest {
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    void setUp() {
        Events.setPublisher(publisher);
    }

    @Test
    @DisplayName("예매에 생성하면, status = BEFORE_PAY")
    void 예매생성_성공() {
        //given
        List<SeatItem> list = List.of(new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));

        //when
        Booking booking = Booking.create(1L, 1L, list);

        //then
        Assertions.assertThat(booking.getStatus()).isEqualTo(BookingStatus.BEFORE_PAY);
    }

    @Test
    @DisplayName("예매 좌석이 4개라면, 예매 생성에 성공한다.")
    void 예매생성_성공_좌석4개() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)),
                new SeatItem(new BookSeatId(2L, 1L), new Money(40000)),
                new SeatItem(new BookSeatId(3L, 1L), new Money(30000)),
                new SeatItem(new BookSeatId(4L, 1L), new Money(20000)));

        //when & then
        assertDoesNotThrow(() -> Booking.create(1L, 1L, list));
    }
    @Test
    @DisplayName("예매 좌석이 4개보다 많다면, 예매 생성에 실패한다.")
    void 예매생성_실패_좌석은최대4개() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)),
                new SeatItem(new BookSeatId(2L, 1L), new Money(40000)),
                new SeatItem(new BookSeatId(3L, 1L), new Money(30000)),
                new SeatItem(new BookSeatId(4L, 1L), new Money(20000)),
                new SeatItem(new BookSeatId(5L, 1L), new Money(10000)));

        //when & then
        assertThrows(InvalidMaxSeatCountException.class,
                () -> Booking.create(1L, 1L, list));
    }
    @Test
    @DisplayName("좌석 정보가 없다면, 예매 생성에 실패한다.")
    void 예매생성_실패_좌석정보가없음() {
        //given
        List<SeatItem> list = List.of();

        //when & then
        assertThrows(InvalidMaxSeatCountException.class,
                () -> Booking.create(1L, 1L, list));
    }

    @Test
    @DisplayName("예매 생성에 성공하면, 좌석 가격의 총합이 계산된다.")
    void 예매생성_성공_총가격() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)),
                new SeatItem(new BookSeatId(2L, 1L), new Money(40000)),
                new SeatItem(new BookSeatId(3L, 1L), new Money(30000)),
                new SeatItem(new BookSeatId(4L, 1L), new Money(20000)));

        //when
        Booking booking = Booking.create(1L, 1L, list);

        //then
        Assertions.assertThat(booking.getTotalAmounts())
                .isEqualTo(new Money(50000 + 40000 + 30000 + 20000));
    }

    @Test
    @DisplayName("예약을 취소하면, status = CANCELED_BOOKING")
    void 예매취소_성공() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        //when
        booking.cancel();

        //then
        Assertions.assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED_BOOKING);
    }

    @Test
    @DisplayName("결제된 예매는 취소할 수 있다.")
    void 예매취소_실패_COMPLETE() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        booking.confirmPayment(1L);

        //when
        assertDoesNotThrow(() -> booking.cancel());
    }

    @Test
    @DisplayName("이미 취소 된 예매는 취소할 수 없다.")
    void 예매취소_실패_CANCELED() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        booking.cancel();

        //when
        assertThrows(AlreadyCanceledException.class, () ->
                booking.cancel());
    }
    @Test
    @DisplayName("예매취소에 성공하면, BookingCanceledEvent가 발생한다.")
    void 예매취소_성공_이벤트발행() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        //when
        booking.cancel();

        //then
        verify(publisher).publishEvent(any(BookingCanceledEvent.class));
    }

    @Test
    @DisplayName("예매에 성공하면, status = COMPLETED_BOOKING")
    void 예매결제_성공() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        //when
        booking.confirmPayment(1L);

        //then
        Assertions.assertThat(booking.getStatus()).isEqualTo(BookingStatus.COMPLETED_BOOKING);
    }

    @Test
    @DisplayName("예매에 성공하면, ConfirmPaymentEvent가 발생된다.")
    void 예매결제_성공_이벤트발행() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);

        //when
        booking.confirmPayment(1L);

        //then
        verify(publisher).publishEvent(any(ConfirmPaymentEvent.class));
    }

    @Test
    @DisplayName("완료된 예매는 예매 완료 할 수 없다.")
    void 예매결제_실패_COMPLETED() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);
        booking.confirmPayment(1L);

        //when & then
        assertThrows(BookingCompletionFailedException.class, () ->
                booking.confirmPayment(1L));
    }

    @Test
    @DisplayName("취소된 예매는 예매 완료 할 수 없다.")
    void 예매결제_실패_CANCELED() {
        //given
        List<SeatItem> list = List.of(
                new SeatItem(new BookSeatId(1L, 1L), new Money(50000)));
        Booking booking = Booking.create(1L, 1L, list);
        booking.cancel();

        //when & then
        assertThrows(BookingCompletionFailedException.class, () ->
                booking.confirmPayment(1L));
    }

}