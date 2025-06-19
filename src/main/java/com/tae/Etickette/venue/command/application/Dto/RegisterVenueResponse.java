package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterVenueResponse {

    private Long id;
    private String place;
    private Integer capacity;
    private Address address;

    @Builder
    public RegisterVenueResponse(Long id, String place, Integer capacity, Address address) {
        this.id = id;
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
}
