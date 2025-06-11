package com.tae.Etickette.venue.application.Dto;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.domain.Venue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VenueCreateRequest {
    @NotBlank
    private final String place;
    @Size(min = 1)
    private final Integer capacity;
    @NotBlank
    private final Address address;


    @Builder
    public VenueCreateRequest(String place, Integer capacity, Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }

    public Venue toEntity() {
        return Venue.create(place, capacity, address);
    }

}
