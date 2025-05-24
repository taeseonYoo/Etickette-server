package com.tae.Etickette.concert.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ConcertTest {

    @Test
    @DisplayName("startAt - year,month 는 고정, day 가 빠른 값이 startAt")
    void startAt_success_day() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(Section.create("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 2), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 3), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getStartAt()).isEqualTo(LocalDate.of(2025, 6, 1));
    }
    @Test
    @DisplayName("startAt - year,day 는 고정, month 가 빠른 값이 startAt")
    void startAt_success_month() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(Section.create("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 3, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 2, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 1, 1), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getStartAt()).isEqualTo(LocalDate.of(2025, 1, 1));
    }
    
}