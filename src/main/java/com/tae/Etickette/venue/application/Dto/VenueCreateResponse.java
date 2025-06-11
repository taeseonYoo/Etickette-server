package com.tae.Etickette.venue.application.Dto;

import com.tae.Etickette.concert.domain.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VenueCreateResponse {

    private Long id;
    private String place;
    private Integer capacity;
    private Address address;

    @Builder
    public VenueCreateResponse(Long id, String place, Integer capacity, Address address) {
        this.id = id;
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
}
