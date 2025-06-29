package com.tae.Etickette.venue.command.domain;

import com.tae.Etickette.concert.command.domain.Address;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit - Venue")
class VenueTest {

    @Test
    @DisplayName("공연장 생성에 성공하면, status = ACTIVE")
    void 공연장생성_성공() {
        //given & when
        Venue venue = Venue.create("KSPO", 10000, new Address("서울시", "송파구 올림픽로 424", "11111"));

        //then
        Assertions.assertThat(venue.getStatus()).isEqualTo(VenueStatus.ACTIVE);
    }

    @Test
    @DisplayName("changeAddress - 주소 변경에 성공한다.")
    void 주소변경_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Address changeAddress = new Address("서울시", "송파구 올림픽로 424", "22222");
        venue.changeAddress(changeAddress);

        //then
        Assertions.assertThat(venue.getAddress()).isEqualTo(changeAddress);
    }

    @Test
    @DisplayName("changeAddress - 이미 삭제된 공연장의 주소를 변경하면, 주소 변경에 실패한다.")
    void 주소변경_실패_DELETED() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(AlreadyDeletedException.class,
                () -> venue.changeAddress(new Address("서울시", "송파구 올림픽로 424", "22222")));
    }

    @Test
    @DisplayName("changeCapacity - 수용량 변경에 성공한다.")
    void 수용량변경_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        venue.changeCapacity(22222);

        //then
        Assertions.assertThat(venue.getCapacity()).isEqualTo(22222);
    }
    @Test
    @DisplayName("changeCapacity - 이미 삭제된 공연장의 수용량을 변경하면, 수용량 변경에 실패한다.")
    void 수용량변경_실패_DELETED() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        venue.deleteVenue();

        //when & then
        assertThrows(AlreadyDeletedException.class,
                () -> venue.changeCapacity(22222));
    }
    @Test
    @DisplayName("changeCapacity - 수용량은 최소 1이상이어야한다.")
    void 수용량변경_실패_최소1() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> venue.changeCapacity(-1));
    }

    @Test
    @DisplayName("deleteVenue - 공연장 삭제에 성공하면, status = DELETED")
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
        assertThrows(AlreadyDeletedException.class,
                () -> venue.deleteVenue());
    }
}