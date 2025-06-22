package com.tae.Etickette.concert.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String imgURL;

    @Enumerated(value = EnumType.STRING)
    private ConcertStatus status;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "grade",
    joinColumns = @JoinColumn(name = "concert_number"))
    private List<GradePrice> gradePrices;

    /**
     * 연관관계
     */
    private Long venueId;

    public Concert(String title, String overview, Integer runningTime, String imgURL,  List<GradePrice> gradePrices,Long venueId) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.imgURL = imgURL;
        this.status = ConcertStatus.BEFORE;

        this.gradePrices = gradePrices;
        //연관관계 설정
        this.venueId = venueId;
    }

    public static Concert create(String title, String overview, Integer runningTime, String imgURL, List<GradePrice> gradePrices,Long venueId) {
        return new Concert(title, overview, runningTime, imgURL, gradePrices,venueId);
    }


    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOverview(String overview) {
        this.overview = overview;
    }



}
