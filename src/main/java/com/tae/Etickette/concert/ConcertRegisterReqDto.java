package com.tae.Etickette.concert;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ConcertRegisterReqDto {

    private final String title;
    private final String overview;
    private final Integer runningTime;
    private final String ImgURL;
    private final List<GradePriceInfo> gradePrices;
    @Builder
    public ConcertRegisterReqDto(String title, String overview, Integer runningTime, String imgURL, List<GradePriceInfo> gradePrices) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        ImgURL = imgURL;
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
