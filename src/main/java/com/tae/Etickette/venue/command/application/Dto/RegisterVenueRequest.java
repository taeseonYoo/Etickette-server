package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.venue.command.domain.Venue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterVenueRequest {
    @NotBlank
    private final String place;
    @Size(min = 1)
    private final Integer capacity;
    @NotBlank
    private final Address address;


    @Builder
    public RegisterVenueRequest(String place, Integer capacity, Address address) {
        this.place = place;
        this.capacity = capacity;
        this.address = address;
    }

    /**
     * 엔티티에 의존하긴 하지만, 편의를 위한 트레이드 오프
     * @return 공연장 엔티티
     */
    public Venue toEntity() {
        return Venue.create(place, capacity, address);
    }

}
