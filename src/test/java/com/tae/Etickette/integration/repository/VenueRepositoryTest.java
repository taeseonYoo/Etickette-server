package com.tae.Etickette.integration.repository;

import com.tae.Etickette.Concert.entity.Address;
import com.tae.Etickette.Concert.entity.Venue;
import com.tae.Etickette.Concert.repository.VenueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class VenueRepositoryTest {

    @Autowired
    VenueRepository venueRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("공연장 저장 - 공연장 저장에 성공한다.")
    public void 공연장_생성_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME(올림픽 체조경기장)", 15000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        //when
        Venue savedVenue = venueRepository.save(venue);

        //then
        Assertions.assertThat(savedVenue).isEqualTo(venue);
    }

    @Test
    @DisplayName("공연장 수정 - 총 좌석 수 수정에 성공한다.")
    public void 공연장_좌석수_수정_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME(올림픽 체조경기장)", 15000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        Venue savedVenue = venueRepository.save(venue);

        //when
        Venue findVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));
        findVenue.updateCapacity(20000);
        em.flush();

        //then
        Venue findUpdateVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));
        Assertions.assertThat(findUpdateVenue.getCapacity()).isEqualTo(20000);
    }
    @Test
    @DisplayName("공연장 수정 - 주소 수정에 성공한다.")
    public void 공연장_주소_수정_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME(올림픽 체조경기장)", 15000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        Venue savedVenue = venueRepository.save(venue);

        //when
        Venue findVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));
        Address address = new Address("서울시", "송파구 방이동 88", "22222");
        findVenue.updateAddress(address);
        em.flush();

        //then
        Venue findUpdateVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));
        Assertions.assertThat(findUpdateVenue.getAddress()).isEqualTo(address);
    }
    @Test
    @DisplayName("공연장 수정 - 총 좌석 수 수정에 실패한다.")
    public void 공연장_좌석수_수정_실패() {
        //given
        Venue venue = Venue.create("KSPO DOME(올림픽 체조경기장)", 15000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        Venue savedVenue = venueRepository.save(venue);

        //when
        Venue findVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));

        assertThrows(IllegalArgumentException.class,
                () -> findVenue.updateCapacity(-1));
        em.flush();

        //then
        Venue findUpdateVenue = venueRepository.findById(savedVenue.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."));
        Assertions.assertThat(findUpdateVenue.getCapacity()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("공연장 삭제 - 공연장 삭제에 성공한다.")
    public void 공연장_삭제_성공() {
        //given
        Venue venue = Venue.create("KSPO DOME(올림픽 체조경기장)", 15000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));
        Venue savedVenue = venueRepository.save(venue);

        //when
        venueRepository.delete(savedVenue);

        //then
        assertThrows(RuntimeException.class,
                () -> venueRepository.findById(savedVenue.getId()).orElseThrow(() -> new RuntimeException("저장 된 공연장이 없습니다."))
        );
    }

}