package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
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
