package com.tae.Etickette.concert.command.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterConcertRequest {
    @Schema(description = "공연 제목", example = "박효신 STPD 2023 GOING HOME")
    private String title;
    @Schema(description = "공연 설명", example = "박효신 2023 팬 콘서트")
    private String overview;
    @Schema(description = "러닝 타임", example = "120")
    private Integer runningTime;
    private List<GradePriceInfo> gradePrices;
    @Schema(description = "공연장Id", example = "1")
    private Long venueId;

    @Builder
    public RegisterConcertRequest(String title, String overview, Integer runningTime, List<GradePriceInfo> gradePrices,
                                  Long venueId) {
        this.title = title;
        this.overview = overview;
        this.runningTime = runningTime;
        this.gradePrices = gradePrices;
        this.venueId = venueId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class GradePriceInfo {
        private String grade;
        private Integer price;

        @Builder
        public GradePriceInfo(String grade, Integer price) {
            this.grade = grade;
            this.price = price;
        }
    }

}
