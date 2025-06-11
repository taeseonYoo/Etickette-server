package com.tae.Etickette.venue.application.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteVenueRequest {
    private Long venueId;

    @Builder
    public DeleteVenueRequest(Long venueId) {
        this.venueId = venueId;
    }


}
