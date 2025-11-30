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
    private final Long concertId;
    private final String title;
    private final String overview;
    private final Integer runningTime;
    private final String imgURL;
    private final List<GradePrice> gradePrices;
    private final ConcertStatus status;

    private final String place;
    private final Integer capacity;
    private final Address address;

    private final List<SessionDetail> sessionDetails;

    public ConcertDetail(Concert concert, VenueData venue, List<SessionDetail> sessionDetails) {
        this.sessionDetails = sessionDetails;
        this.concertId = concert.getId();
        this.title = concert.getTitle();
        this.overview = concert.getOverview();
        this.runningTime = concert.getRunningTime();
        this.imgURL = concert.getImage().getPath();
        this.gradePrices = concert.getGradePrices();
        this.status = concert.getStatus();

        this.place = venue.getPlace();
        this.capacity = venue.getCapacity();
        this.address = venue.getAddress();
    }
}
