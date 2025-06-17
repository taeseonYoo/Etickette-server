package com.tae.Etickette.venue.domain;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.command.domain.AlreadyDeletedException;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.command.domain.VenueStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VenueTest {
    @Test
    @DisplayName("changeAddress - 주소 변경에 성공한다.")
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
    @DisplayName("changeAddress - 주소가 null 이면, 주소 변경에 실패한다.")
    void 주소변경_실패_null() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> venue.changeAddress(null));
    }

    @Test
    @DisplayName("changeAddress - 이미 DELETE된 공연장의 주소를 변경하면, 주소 변경에 실패한다.")
    void 주소변경_실패_삭제된공연장() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(AlreadyDeletedException.class,
                () -> venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "11111")));
    }

    @Test
    @DisplayName("changeAddress - 주소 변경에 실패하면, 주소는 변경 되지 않는다.")
    void 주소변경_실패_변경X() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(AlreadyDeletedException.class,
                () -> venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "11111")));
        Assertions.assertThat(venue.getAddress().getZipcode()).isEqualTo("11111");
    }

    @Test
    @DisplayName("deleteVenue - 공연장 삭제에 성공한다.")
    void 공연장삭제_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        venue.deleteVenue();

        //then
        Assertions.assertThat(venue.getStatus()).isEqualTo(VenueStatus.DELETED);
    }
    @Test
    @DisplayName("deleteVenue - 이미 삭제된 공연장을 삭제하면, 공연장 삭제에 실패한다.")
    void 공연장삭제_실패_이미삭제됨() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(AlreadyDeletedException.class, () ->
                venue.deleteVenue());
    }

}