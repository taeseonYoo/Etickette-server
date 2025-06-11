package com.tae.Etickette.helper;


import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.application.Dto.VenueCreateRequestDto;

public class VenueCreateReqDtoBuilder {
    private String place = "KSPO";
    private Integer capacity = 10000;
    private Address address = new Address("서울시", "송파구 올림픽로 424", "11111");

    @Builder
    public VenueCreateReqDtoBuilder(String place, Integer capacity, Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }
}
