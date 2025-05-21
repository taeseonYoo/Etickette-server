package com.tae.Etickette.concert.application;

import com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.concert.domain.Section;
import com.tae.Etickette.concert.domain.Venue;
import com.tae.Etickette.concert.infra.SectionRepository;
import com.tae.Etickette.concert.infra.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tae.Etickette.concert.application.Dto.VenueCreateRequestDto.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("Unit - VenueService")
class VenueServiceTest {
    private VenueService venueService;
    private final VenueRepository mockVenueRepo = Mockito.mock(VenueRepository.class);
    private final SectionRepository mockSectionRepo = Mockito.mock(SectionRepository.class);

    @BeforeEach
    public void setup() {
        venueService = new VenueService(mockVenueRepo, mockSectionRepo);
    }

    @Test
    @DisplayName("")
    public void 공연장등록_실패_주소가존재함() {
        //given
        List<SectionRequestDto> sections = new ArrayList<>();
        sections.add(SectionRequestDto.builder().grade("VIP").price(150000).build());
        sections.add(SectionRequestDto.builder().grade("A").price(100000).build());
        sections.add(SectionRequestDto.builder().grade("R").price(50000).build());

        VenueCreateRequestDto oldDto = builder()
                .place("KSPO DOME")
                .capacity(10000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .sections(sections)
                .build();
        VenueCreateRequestDto requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .sections(sections)
                .build();

        Venue oldVenue = oldDto.toEntity();
        Venue venue = requestDto.toEntity();

        BDDMockito.given(mockVenueRepo.findVenueByAddress(any()))
                .willReturn(Optional.of(oldVenue));
        BDDMockito.given(mockVenueRepo.save(any()))
                .willReturn(venue);

        //when & then
        Assertions.assertThrows(DuplicateKeyException.class,
                () -> venueService.createVenue(requestDto));
    }

}