package com.tae.Etickette.concert.application.Dto;

import com.tae.Etickette.concert.domain.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VenueCreateResponseDto {

    private Long id;
    private String place;
    private Integer capacity;
    private Address address;

    @Builder
    public VenueCreateResponseDto(Long id, String place, Integer capacity, Address address) {
        this.id = id;
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
}
