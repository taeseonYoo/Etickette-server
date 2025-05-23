package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @OneToMany(mappedBy = "concert")
    private List<Section> sections = new ArrayList<>();
    @OneToMany(mappedBy = "concert")
    private List<Schedule> schedules = new ArrayList<>();

    private Concert(String title, String overview, LocalDate startAt, LocalDate endAt,Integer runningTime) {
        this.title = title;
        this.overview = overview;
        this.startAt = startAt;
        this.endAt = endAt;
        this.runningTime = runningTime;
        this.status = ConcertStatus.PENDING;
    }

    public static Concert create(String title, String overview
            , LocalDate startAt, LocalDate endAt,Integer runningTime) {
        return new Concert(title, overview, startAt, endAt,runningTime);
    }

    //공연장 - 콘서트 연관관계 편의 메서드
    public void addVenue(Venue venue) {
        this.venue = venue;
        venue.getConcerts().add(this);
    }

    public void addSection(Section section) {
        section.setConcert(this);
        this.sections.add(section);
    }

    public void addSchedule(Schedule schedule) {
        schedule.setConcert(this);
        this.schedules.add(schedule);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOverview(String overview) {
        this.overview = overview;
    }



}
