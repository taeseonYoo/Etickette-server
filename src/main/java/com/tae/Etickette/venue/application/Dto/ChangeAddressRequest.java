package com.tae.Etickette.venue.application.Dto;

import com.tae.Etickette.concert.domain.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeAddressRequest {
    @NotBlank
    private Address address;

    @Builder
    public ChangeAddressRequest(Address address) {
        this.address = address;
    }
}
