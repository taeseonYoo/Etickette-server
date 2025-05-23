package com.tae.Etickette.concert.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    @Column
    private String overview;
    @Column(nullable = false)
    private LocalDate startAt;
    @Column(nullable = false)
    private LocalDate endAt;
    @Column
    private ConcertStatus status;
    @Column
    private String ImgURL;

    /**
     * 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    //공연장 - 콘서트 연관관계 편의 메서드
    public void addVenue(Venue venue) {
        this.venue = venue;
        venue.getConcerts().add(this);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOverview(String overview) {
        this.overview = overview;
    }

    private Concert(String title, String overview, LocalDate startAt, LocalDate endAt) {
        this.title = title;
        this.overview = overview;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = ConcertStatus.PENDING;
    }

    public static Concert create(String title, String overview
            , LocalDate startAt, LocalDate endAt) {
        return new Concert(title, overview, startAt, endAt);
    }

}
