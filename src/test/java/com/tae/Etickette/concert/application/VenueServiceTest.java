package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.concert.application.Dto.VenueCreateResponseDto;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Seat;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

import static com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@DisplayName("Unit - VenueService")
class VenueServiceTest {
    private VenueService venueService;
    private final VenueRepository mockVenueRepo = mock(VenueRepository.class);

    @BeforeEach
    public void setup() {
        venueService = new VenueService(mockVenueRepo);
    }

    @Test
    @DisplayName("createVenue - 공연장 등록에 성공한다.")
    public void 공연장등록_성공() {
        //given
        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME")
                .capacity(10000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        Venue venue = requestDto.toEntity();

        BDDMockito.given(mockVenueRepo.findVenueByAddress(any()))
                .willReturn(Optional.empty());
        BDDMockito.given(mockVenueRepo.save(any()))
                .willReturn(venue);

        //when
        VenueCreateResponseDto responseDto = venueService.createVenue(requestDto);

        //then
        assertThat(responseDto.getPlace()).isEqualTo("KSPO DOME");
    }
    @Test
    @DisplayName("createVenue - 주소가 존재하면, 공연장 등록에 실패한다.")
    public void 공연장등록_실패_주소가존재함() {
        //given
        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME")
                .capacity(10000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        Venue venue = requestDto.toEntity();

        BDDMockito.given(mockVenueRepo.findVenueByAddress(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(DuplicateKeyException.class,
                () -> venueService.createVenue(requestDto));
    }

}