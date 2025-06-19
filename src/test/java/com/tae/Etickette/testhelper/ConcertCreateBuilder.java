package com.tae.Etickette.testhelper;

import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;

import java.util.List;

import static com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest.*;

public class ConcertCreateBuilder {
    private String title = "IU HER";
    private String overview = "아이유 공연";
    private Integer runningTime = 120;
    private String imgURL = "";
    private List<GradePriceInfo> gradePriceInfos = List.of(
            GradePriceInfo.builder().grade("VIP").price(150000).build(),
            GradePriceInfo.builder().grade("S").price(100000).build(),
            GradePriceInfo.builder().grade("R").price(50000).build()
    );

    public static ConcertCreateBuilder builder() {
        return new ConcertCreateBuilder();
    }
    public RegisterConcertRequest build() {
        return RegisterConcertRequest.builder()
                .title(title)
                .overview(overview)
                .runningTime(runningTime)
                .imgURL(imgURL)
                .gradePrices(gradePriceInfos)
                .build();
    }

    public ConcertCreateBuilder title(String title) {
        this.title = title;
        return this;
    }
    public ConcertCreateBuilder overview(String overview) {
        this.overview = overview;
        return this;
    }
    public ConcertCreateBuilder runningTime(Integer runningTime) {
        this.runningTime = runningTime;
        return this;
    }
    public ConcertCreateBuilder imgURL(String imgURL) {
        this.imgURL = imgURL;
        return this;
    }
    public ConcertCreateBuilder gradePrices(List<GradePriceInfo> gradePriceInfos) {
        this.gradePriceInfos = gradePriceInfos;
        return this;
    }


}
