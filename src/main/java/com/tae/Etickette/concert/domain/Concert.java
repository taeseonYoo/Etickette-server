package com.tae.Etickette.concert.domain;

import com.tae.Etickette.schedule.domain.Schedule;
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
    private Integer runningTime;
    @Column
    private String ImgURL;
    @Column
    private ConcertStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "grade",
    joinColumns = @JoinColumn(name = "concert_number"))
    private List<Grade> grades;

    /**
     * 연관관계
     */
    private Long venueId;


    public Concert(String title, String overview, Integer runningTime, String imgURL, Long venueId, List<Grade> grades) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.ImgURL = imgURL;
        this.status = ConcertStatus.BEFORE;
        this.venueId = venueId;

        this.grades = grades;
        //연관관계 설정
    }

    public static Concert create(String title, String overview, Integer runningTime, String imgURL, Long venueId
            , List<Grade> grades) {
        return new Concert(title, overview, runningTime, imgURL, venueId, grades);
    }


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOverview(String overview) {
        this.overview = overview;
    }



}
