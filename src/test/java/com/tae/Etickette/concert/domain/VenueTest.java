package com.tae.Etickette.concert.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VenueTest {

    @Test
    void 주소변경_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "22222"));

        //then
        Assertions.assertThat(venue.getAddress().getZipcode()).isEqualTo("22222");
    }

    @Test
    void 주소변경_실패_null() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> venue.changeAddress(null));
    }

    @Test
    void 주소변경_실패_삭제된공연장() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(VenueAlreadyDeletedException.class,
                () -> venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "11111")));
    }

    @Test
    @DisplayName("주소 변경에 실패하면, 주소는 변경 되지 않는다.")
    void 주소변경_실패_변경X() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(VenueAlreadyDeletedException.class,
                () -> venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "11111")));
        Assertions.assertThat(venue.getAddress().getZipcode()).isEqualTo("11111");
    }
}