package com.tae.Etickette.concert.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RegisterConcertRequest {

    private final String title;
    private final String overview;
    private final Integer runningTime;
    private final String imgURL;
    private final List<GradePriceInfo> gradePrices;
    @Builder
    public RegisterConcertRequest(String title, String overview, Integer runningTime, String imgURL, List<GradePriceInfo> gradePrices) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.imgURL = imgURL;
        this.gradePrices = gradePrices;
    }

    @Getter
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
