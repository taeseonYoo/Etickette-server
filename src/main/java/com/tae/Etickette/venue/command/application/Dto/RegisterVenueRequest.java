package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterVenueRequest {
    @NotBlank
    private final String place;
    @Size(min = 0)
    private final Integer capacity;
    @NotBlank
    private final Address address;


    @Builder
    public RegisterVenueRequest(String place, Integer capacity, Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }

}
