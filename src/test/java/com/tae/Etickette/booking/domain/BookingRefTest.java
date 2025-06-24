package com.tae.Etickette.booking.domain;

import com.tae.Etickette.booking.command.domain.BookingRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BookingRef")
class BookingRefTest {

    @Test
    @DisplayName("예매번호의 앞자리는 MMdd의 값을 나타낸다.")
    void id생성_성공() {
        //given
        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));

        //when
        BookingRef bookingRef = BookingRef.generate();

        //then
        Assertions.assertTrue(bookingRef.getValue().startsWith(formatDate));
    }

    @Test
    @DisplayName("value 값이 같으면, 동등성 비교에 성공한다.")
    void 동등성비교_성공() {
        //given
        BookingRef bookingRef = BookingRef.generate();

        BookingRef newRef = new BookingRef(bookingRef.getValue());

        //when & then
        assertThat(bookingRef).isEqualTo(newRef);
    }
}