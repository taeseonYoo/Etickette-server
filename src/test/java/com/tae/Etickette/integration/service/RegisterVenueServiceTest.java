package com.tae.Etickette.integration.service;

import com.tae.Etickette.venue.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.venue.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.venue.application.RegisterVenueService;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.application.VenueQueryService;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static com.tae.Etickette.venue.application.Dto.VenueCreateRequestDto.builder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Integration - Venue")
public class RegisterVenueServiceTest {
    @Autowired
    private RegisterVenueService registerVenueService;
    @Autowired
    private VenueQueryService venueQueryService;
    @Autowired
    private VenueRepository venueRepository;

    @Test
    @DisplayName("register - 공연장 등록에 성공한다.")
    public void 공연장등록_성공() {

        //given
        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        //when
        VenueCreateResponseDto responseDto = registerVenueService.register(requestDto);
        Venue findVenue = venueQueryService.findById(responseDto.getId());

        //then
        Assertions.assertThat(findVenue.getPlace()).isEqualTo("KSPO DOME(올림픽 체조경기장)");
        Assertions.assertThat(findVenue.getAddress().getZipcode()).isEqualTo("11111");
    }

    @Test
    @DisplayName("register - 주소가 이미 등록되어 있다면, 공연장 등록에 실패한다.")
    public void 공연장등록_실패_중복된주소() {
        //given
        venueRepository.save(Venue.create("KSPO", 15000, new Address("서울시", "송파구 올림픽로 424", "11111")));

        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        //when & then
        assertThrows(DuplicateKeyException.class,
                () -> registerVenueService.register(requestDto));
    }
}
