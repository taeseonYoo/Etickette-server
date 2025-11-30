package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterVenueRequest {
    @NotBlank
    @Schema(description = "장소명",example = "KSPO DOME")
    private final String place;
    @Min(value = 0)
    @Schema(description = "수용 인원",example = "10000")
    private final Integer capacity;
    @Schema(description = "주소",example = "서울특별시 송파구 올림픽로 424")
    private final Address address;


    @Builder
    public RegisterVenueRequest(String place, Integer capacity, Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }

}
