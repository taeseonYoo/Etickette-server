package com.tae.Etickette.testhelper;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;

public class VenueCreateBuilder {
    private String place = "KSPO";
    private Integer capacity = 10000;
    private Address address = new Address("서울시", "송파구","11111");

    public static VenueCreateBuilder builder() {
        return new VenueCreateBuilder();
    }

    public RegisterVenueRequest build() {
        return RegisterVenueRequest.builder()
                .place(place)
                .capacity(capacity)
                .address(address)
                .build();
    }
    public VenueCreateBuilder place(String place) {
        this.place = place;
        return this;
    }
    public VenueCreateBuilder capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }
    public VenueCreateBuilder address(Address address) {
        this.address = address;
        return this;
    }

}
