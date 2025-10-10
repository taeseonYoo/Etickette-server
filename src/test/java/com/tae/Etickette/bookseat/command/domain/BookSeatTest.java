package com.tae.Etickette.bookseat.command.domain;

import com.tae.Etickette.global.exception.ConflictException;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.model.Money;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Unit - BookSeat")
class BookSeatTest {

    @Test
    @DisplayName("잠금 된 좌석은 예매할 수 있다.")
    void reserve_success() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();

        //when
        bookSeat.reserve();

        //then
        Assertions.assertThat(bookSeat.getStatus()).isEqualTo(SeatStatus.BOOKED);
    }
    @Test
    @DisplayName("상태가 AVAILABLE인 좌석은 예매할 수 없다.")
    void reserve_fail_available() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));

        //when & then
        Assertions.assertThatThrownBy(bookSeat::reserve)
                .isInstanceOf(ConflictException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_NOT_LOCKED);
    }
    @Test
    @DisplayName("상태가 BOOKED인 좌석은 예매할 수 없다.")
    void reserve_fail_BOOKED() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();
        bookSeat.reserve();

        //when & then
        Assertions.assertThatThrownBy(bookSeat::reserve)
                .isInstanceOf(ConflictException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_NOT_LOCKED);
    }
    @Test
    @DisplayName("좌석을 잠금하면, 상태가 LOCKED로 변경된다.")
    void lock_success() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));

        //when
        bookSeat.lock();

        //then
        Assertions.assertThat(bookSeat.getStatus()).isEqualTo(SeatStatus.LOCKED);
    }
    @Test
    @DisplayName("예약된 좌석은 좌석을 락 할 수없다.")
    void lock_fail_BOOKED() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();
        bookSeat.reserve();

        //when & then
        Assertions.assertThatThrownBy(bookSeat::lock)
                .isInstanceOf(ConflictException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_ALREADY_RESERVED);
    }
    @Test
    @DisplayName("락 된 좌석을 락 할 수없다.")
    void lock_fail_LOCKED() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();

        //when & then
        Assertions.assertThatThrownBy(bookSeat::lock)
                .isInstanceOf(ConflictException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_ALREADY_RESERVED);
    }
    @Test
    @DisplayName("선점 된 좌석을 취소하면, AVAILABLE 이 된다.")
    void cancel_success_LOCKED() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();

        //when
        bookSeat.cancel();

        //then
        Assertions.assertThat(bookSeat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
    }
    @Test
    @DisplayName("예약 된 좌석을 취소하면, AVAILABLE 이 된다.")
    void cancel_success_BOOKED() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));
        bookSeat.lock();
        bookSeat.reserve();

        //when
        bookSeat.cancel();

        //then
        Assertions.assertThat(bookSeat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
    }
    @Test
    @DisplayName("선점되지 않은 좌석은 취소할 수 없다.")
    void cancel_fail_AVAILABLE() {
        //given
        BookSeat bookSeat = BookSeat.create(1L, 1L, "VIP", new Money(10000));

        Assertions.assertThatThrownBy(bookSeat::cancel)
                .isInstanceOf(ConflictException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.BOOKSEAT_CANNOT_BE_CANCELED);
    }

}