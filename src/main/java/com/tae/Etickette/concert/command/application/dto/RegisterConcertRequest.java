package com.tae.Etickette.concert.command.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterConcertRequest {

    private String title;
    private String overview;
    private Integer runningTime;
    private String imgURL;
    private List<GradePriceInfo> gradePrices;
    private Long venueId;

    @Builder
    public RegisterConcertRequest(String title, String overview, Integer runningTime, String imgURL, List<GradePriceInfo> gradePrices, Long venueId) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.imgURL = imgURL;
        this.gradePrices = gradePrices;
        this.venueId = venueId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GradePriceInfo{
        private String grade;
        private Integer price;

        @Builder
        public GradePriceInfo(String grade, Integer price) {
            this.grade = grade;
            this.price = price;
        }
    }

}
