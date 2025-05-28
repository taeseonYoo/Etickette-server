package com.tae.Etickette.concert.domain;

import com.tae.Etickette.schedule.domain.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ConcertTest {

    @Test
    @DisplayName("startAt - year,month 는 고정, day 가 작은 값이 startAt")
    void startAt_day() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 2), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 3), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getStartAt()).isEqualTo(LocalDate.of(2025, 6, 1));
    }
    @Test
    @DisplayName("startAt - year,day 는 고정, month 가 작은 값이 startAt")
    void startAt_month() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 3, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 2, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 1, 1), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getStartAt()).isEqualTo(LocalDate.of(2025, 1, 1));
    }
    @Test
    @DisplayName("startAt - month,day 는 고정, year 가 작은 값이 startAt")
    void startAt_year() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2023, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2024, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 1), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getStartAt()).isEqualTo(LocalDate.of(2023, 6, 1));
    }

    @Test
    @DisplayName("endAt - year,month 는 고정, day 가 큰 값이 endAt")
    void endAt_day() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 2), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 3), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getEndAt()).isEqualTo(LocalDate.of(2025, 6, 3));
    }
    @Test
    @DisplayName("endAt - year,day 는 고정, month 가 큰 값이 endAt")
    void endAt_month() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2025, 3, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 2, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 1, 1), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getEndAt()).isEqualTo(LocalDate.of(2025, 3, 1));
    }
    @Test
    @DisplayName("endAt - month,day 는 고정, year 가 큰 값이 endAt")
    void endAt_year() {
        //given
        Venue venue = Venue.create("KSPO DOME", 10000,
                new Address("서울시", "송파구 올림픽로 424", "11111"));

        //when
        Concert concert = Concert.create("Test Title",
                "Test Overview",
                120,
                "img.png",
                venue,
                List.of(new Grade("VIP", 100000)),
                List.of(Schedule.create(LocalDate.of(2023, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2024, 6, 1), LocalTime.of(16, 0)),
                        Schedule.create(LocalDate.of(2025, 6, 1), LocalTime.of(16, 0)))
        );

        //then
        assertThat(concert.getEndAt()).isEqualTo(LocalDate.of(2025, 6, 1));
    }

}