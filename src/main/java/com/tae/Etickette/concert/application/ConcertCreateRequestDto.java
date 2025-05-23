package com.tae.Etickette.concert.application;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ConcertCreateRequestDto {
    @NotEmpty
    private Long venueId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String overview;
    @NotEmpty
    private Integer runningTime;

    private List<ScheduleInfo> scheduleInfos;
    private List<SectionInfo> sectionInfos;

    @Getter
    public static class ScheduleInfo {
        private LocalDate date;
        private LocalTime time;
    }

    @Getter
    public static class SectionInfo{
        private String grade;
        private Integer price;
    }

}
