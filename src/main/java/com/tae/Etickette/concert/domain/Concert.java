package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String overview;
    @Column(nullable = false)
    private LocalDate startAt;
    @Column(nullable = false)
    private LocalDate endAt;
    @Column(nullable = false)
    private Integer runningTime;
    @Column
    private String ImgURL;
    @Column
    private ConcertStatus status;

    /**
     * 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();
    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();


    public Concert(String title, String overview, Integer runningTime, String imgURL, Venue venue, List<Section> sections, List<Schedule> schedules) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.ImgURL = imgURL;
        this.status = ConcertStatus.BEFORE_OPENING;

        this.startAt = getMinLocalDate(schedules);
        this.endAt = getMaxLocalDate(schedules);

        //연관관계 설정
        addVenue(venue);
        schedules.forEach(this::addSchedule);
        sections.forEach(this::addSection);
    }

    public static Concert create(String title, String overview, Integer runningTime, String imgURL, Venue venue
            , List<Section> sections, List<Schedule> schedules) {
        return new Concert(title, overview, runningTime, imgURL, venue, sections, schedules);
    }
    //마지막 공연 일자
    private LocalDate getMaxLocalDate(List<Schedule> schedules) {
        return schedules.stream()
                .map(Schedule::getConcertDate)
                .max(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("스케줄 정보가 없습니다."));
    }
    //첫 공연 일자
    private LocalDate getMinLocalDate(List<Schedule> schedules) {
        return schedules.stream()
                .map(Schedule::getConcertDate)
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("스케줄 정보가 없습니다."));
    }

    public void validateNoDuplicateSchedules() {
    }

    public Boolean verifyNotFinished() {
        if (this.status == ConcertStatus.FINISHED) {
            return false;
        }
        return true;
    }

    //공연장 - 콘서트 연관관계 편의 메서드
    public void addVenue(Venue venue) {
        this.venue = venue;
        venue.getConcerts().add(this);
    }

    public void addSection(Section section) {
        section.addConcert(this);
        this.sections.add(section);
    }

    public void addSchedule(Schedule schedule) {
        schedule.addConcert(this);
        this.schedules.add(schedule);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOverview(String overview) {
        this.overview = overview;
    }



}
