package com.tae.Etickette.venue.command.application.Dto;

import com.tae.Etickette.concert.command.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeAddressRequest {
    @Schema(description = "변경 할 주소", example = "서울특별시 송파구 올림픽로 424")
    private final Address address;

    @Builder
    public ChangeAddressRequest(Address address) {
        this.address = address;
    }
}
