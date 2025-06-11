package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.application.Dto.DeleteVenueRequest;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.domain.VenueStatus;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - DeleteVenueService")
class DeleteVenueServiceTest {

    @InjectMocks
    private DeleteVenueService deleteVenueService;
    private final VenueRepository venueRepository = mock(VenueRepository.class);

    @BeforeEach
    void setUp() {
        deleteVenueService = new DeleteVenueService(venueRepository);
    }

    @Test
    @DisplayName("delete - 공연장 삭제에 성공한다.")
    void 삭제_성공() {
        //given
        DeleteVenueRequest requestDto = DeleteVenueRequest.builder().venueId(1L).build();
        Venue venue = Venue.create("KSPO", 15000, new Address("서울시", "송파구 올림픽로 424", "11111"));

        BDDMockito.given(venueRepository.findById(any()))
                .willReturn(Optional.of(venue));

        //when
        deleteVenueService.delete(requestDto);

        //then
        Assertions.assertThat(venue.getStatus()).isEqualTo(VenueStatus.DELETE);
    }

    @Test
    @DisplayName("delete - 공연장이 없다면, 공연장 삭제에 실패한다.")
    void 삭제_실패_공연장이없음() {
        //given
        DeleteVenueRequest requestDto = DeleteVenueRequest.builder().venueId(1L).build();

        BDDMockito.given(venueRepository.findById(any()))
                .willReturn(Optional.empty());

        //when & then
        assertThrows(VenueNotFoundException.class, () ->
                deleteVenueService.delete(requestDto));
    }



}