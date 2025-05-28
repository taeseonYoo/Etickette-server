package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.domain.Grade;
import com.tae.Etickette.schedule.domain.Schedule;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private String imgUrl;

    private List<ScheduleInfo> scheduleInfos;
    private List<SectionInfo> sectionInfos;

    @Builder
    public ConcertCreateRequestDto(Long venueId, String title, String overview, Integer runningTime,String imgUrl, List<ScheduleInfo> scheduleInfos, List<SectionInfo> sectionInfos) {
        this.venueId = venueId;
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.imgUrl = imgUrl;
        this.scheduleInfos = scheduleInfos;
        this.sectionInfos = sectionInfos;
    }
    @Getter
    @AllArgsConstructor
    public static class ScheduleInfo {
        private LocalDate date;
        private LocalTime time;
    }

    @Getter
    @AllArgsConstructor
    public static class SectionInfo{
        private String grade;
        private Integer price;
    }

    public List<Schedule> toScheduleEntities() {
        return scheduleInfos.stream()
                .map(info -> Schedule.create(info.getDate(), info.getTime(), runningTime, venueId))
                .toList();
    }

    public List<Grade> toSectionEntities() {
        return sectionInfos.stream()
                .map(info -> new Grade(info.getGrade(), info.getPrice()))
                .toList();
    }
}
