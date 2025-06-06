package com.tae.Etickette.venue.application.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeAddressRequestDto {
    private Long venueId;
    private String city;
    private String street;
    private String zipcode;

    @Builder
    public ChangeAddressRequestDto(Long venueId, String city, String street, String zipcode) {
        this.venueId = venueId;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
