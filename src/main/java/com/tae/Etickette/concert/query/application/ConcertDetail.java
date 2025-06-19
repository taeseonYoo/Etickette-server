package com.tae.Etickette.concert.query.application;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.command.domain.ConcertStatus;
import com.tae.Etickette.concert.command.domain.GradePrice;
import com.tae.Etickette.venue.query.VenueData;
import lombok.Getter;

import java.util.List;

@Getter
public class ConcertDetail {
    private Long concertId;
    private String title;
    private String overview;
    private Integer runningTime;
    private String imgURL;
    private List<GradePrice> gradePrices;
    private ConcertStatus status;

    private String place;
    private Integer capacity;
    private Address address;

    private List<SessionDetail> sessionDetails;

    public ConcertDetail(Concert concert, VenueData venue, List<SessionDetail> sessionDetails) {
        this.sessionDetails = sessionDetails;
        this.concertId = concert.getId();
        this.title = concert.getTitle();
        this.overview = concert.getOverview();
        this.runningTime = concert.getRunningTime();
        this.imgURL = concert.getImgURL();
        this.gradePrices = concert.getGradePrices();
        this.status = concert.getStatus();

        this.place = venue.getPlace();
        this.capacity = venue.getCapacity();
        this.address = venue.getAddress();
    }
}
