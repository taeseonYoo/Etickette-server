package com.tae.Etickette.integration.service;

import com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.concert.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.concert.application.VenueService;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Section;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.SectionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto.builder;

@SpringBootTest
@Transactional
@DisplayName("Integration - Venue")
public class VenueServiceTest {
    @Autowired
    private VenueService venueService;

    @Test
    @DisplayName("createVenue")
    public void 공연장등록_성공() {

        //given
        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        //when
        VenueCreateResponseDto responseDto = venueService.createVenue(requestDto);
        Venue findVenue = venueService.findById(responseDto.getId());

        //then
        Assertions.assertThat(findVenue.getPlace()).isEqualTo("KSPO DOME(올림픽 체조경기장)");
        Assertions.assertThat(findVenue.getAddress().getZipcode()).isEqualTo("11111");
    }
}
