package com.tae.Etickette.integration.service;

import com.tae.Etickette.concert.application.ConcertCreateRequestDto;
import com.tae.Etickette.concert.application.ConcertService;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@DisplayName("Integration - ConcertService")
public class ConcertServiceTest {
    @Autowired
    private ConcertService concertService;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @PersistenceContext
    EntityManager em;

    Venue venue;
    List<ConcertCreateRequestDto.ScheduleInfo> scheduleInfos;
    List<ConcertCreateRequestDto.SectionInfo> sectionInfos;

    @BeforeEach
    void setUp() {
        venue = venueRepository.save(Venue.create("KSPO DOME(올림픽 체조경기장)",
                10000, new Address("서울시", "송파구 올림픽로 424", "11111")));

        scheduleInfos = List.of(
                new ConcertCreateRequestDto.ScheduleInfo(LocalDate.of(2025, 6, 1), LocalTime.of(19, 0)),
                new ConcertCreateRequestDto.ScheduleInfo(LocalDate.of(2025, 6, 2), LocalTime.of(18, 30))
        );

        sectionInfos = List.of(
                new ConcertCreateRequestDto.SectionInfo("VIP", 150000),
                new ConcertCreateRequestDto.SectionInfo("R", 100000)
        );
    }


    @Test
    void 콘서트생성_성공() {
        //given
        ConcertCreateRequestDto requestDto = ConcertCreateRequestDto.builder()
                .venueId(venue.getId())
                .title("첫번째 공연")
                .overview("처음으로 하는 공연입니다.")
                .runningTime(120)
                .scheduleInfos(scheduleInfos)
                .sectionInfos(sectionInfos)
                .build();

        //when
        Long savedId = concertService.createConcert(requestDto);

        //then
        Assertions.assertThat(concertRepository.findById(savedId)).isPresent();
    }

    @Test
    void 콘서트생성_성공_연관관계() {
        //given
        ConcertCreateRequestDto requestDto = ConcertCreateRequestDto.builder()
                .venueId(venue.getId())
                .title("첫번째 공연")
                .overview("처음으로 하는 공연입니다.")
                .runningTime(120)
                .scheduleInfos(scheduleInfos)
                .sectionInfos(sectionInfos)
                .build();

        //when
        Long savedId = concertService.createConcert(requestDto);
        em.flush();
        em.clear();
        Concert concert = concertRepository.findById(savedId).get();

        //then
//        concert.getSchedules().forEach(schedule->
//                Assertions.assertThat(schedule.getConcert()).isEqualTo(concert));

    }

    @Test
    void 콘서트생성_실패() {
        ConcertCreateRequestDto requestDto = ConcertCreateRequestDto.builder()
                .venueId(venue.getId())
                .title("첫번째 공연")
                .overview("처음으로 하는 공연입니다.")
                .runningTime(120)
                .scheduleInfos(scheduleInfos)
                .sectionInfos(sectionInfos)
                .build();
        Long savedId = concertService.createConcert(requestDto);
        em.flush();
        em.clear();

        ConcertCreateRequestDto requestDto2 = ConcertCreateRequestDto.builder()
                .venueId(venue.getId())
                .title("두번째 공연")
                .overview("두번째로 하는 공연입니다.")
                .runningTime(120)
                .scheduleInfos(scheduleInfos)
                .sectionInfos(sectionInfos)
                .build();
        //when
        assertThrows(RuntimeException.class,
                () -> concertService.createConcert(requestDto2));

    }


}
