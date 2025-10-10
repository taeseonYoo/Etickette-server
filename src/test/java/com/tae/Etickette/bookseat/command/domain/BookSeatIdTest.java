package com.tae.Etickette.bookseat.command.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit - BookSeatId")
class BookSeatIdTest {
    private final Long SEAT_ID_1 = 101L;
    private final Long SEAT_ID_2 = 102L;
    private final Long SESSION_ID_1 = 201L;
    private final Long SESSION_ID_2 = 202L;
    @Test
    @DisplayName("반사성 : 자기 자신과 같아야 한다.")
    void Reflexivity() {
        //given
        BookSeatId bookSeatId = new BookSeatId(SEAT_ID_1, SESSION_ID_1);

        //when & then
        Assertions.assertThat(bookSeatId.equals(bookSeatId)).isTrue();
        Assertions.assertThat(bookSeatId.hashCode()).isEqualTo(bookSeatId.hashCode());
    }
    @Test
    @DisplayName("대칭성 : A.equals(B)가 참이면 B.equals(A)도 참이어야 한다.")
    void Symmetry() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        BookSeatId bookSeatId2 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        //when & then
        Assertions.assertThat(bookSeatId1.equals(bookSeatId2)).isTrue();
        Assertions.assertThat(bookSeatId2.equals(bookSeatId1)).isTrue();
    }
    @Test
    @DisplayName("seat_id가 다른 객체는 달라야 한다.")
    void equals_fail_seat_id() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        BookSeatId bookSeatId2 = new BookSeatId(SEAT_ID_2, SESSION_ID_1);
        //when & then
        Assertions.assertThat(bookSeatId1.equals(bookSeatId2)).isFalse();
    }
    @Test
    @DisplayName("session_id가 다른 객체는 달라야 한다.")
    void equals_fail_session_id() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        BookSeatId bookSeatId2 = new BookSeatId(SEAT_ID_1, SESSION_ID_2);
        //when & then
        Assertions.assertThat(bookSeatId1.equals(bookSeatId2)).isFalse();
    }
    @Test
    @DisplayName("seat_id가 다른 객체의 hashcode는 달라야한다..")
    void hashcode_seat_id() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        BookSeatId bookSeatId2 = new BookSeatId(SEAT_ID_2, SESSION_ID_1);
        //when & then
        Assertions.assertThat(bookSeatId1.hashCode()).isNotEqualTo(bookSeatId2.hashCode());
    }
    @Test
    @DisplayName("session_id가 다른 객체의 hashcode는 달라야한다.")
    void hashcode_session_id() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);
        BookSeatId bookSeatId2 = new BookSeatId(SEAT_ID_1, SESSION_ID_2);
        //when & then
        Assertions.assertThat(bookSeatId1.hashCode()).isNotEqualTo(bookSeatId2.hashCode());
    }
    @Test
    @DisplayName("null과 비교하면 false를 반환해야한다.")
    void equals_null() {
        //given
        BookSeatId bookSeatId1 = new BookSeatId(SEAT_ID_1, SESSION_ID_1);

        //when & then
        Assertions.assertThat(bookSeatId1.equals(null)).isFalse();
    }
}