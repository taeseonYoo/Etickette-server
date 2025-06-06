package com.tae.Etickette.venue.application.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteVenueReqDto {
    private Long venueId;

    @Builder
    public DeleteVenueReqDto(Long venueId) {
        this.venueId = venueId;
    }


}
