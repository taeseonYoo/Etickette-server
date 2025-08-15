package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.venue.command.application.DeleteVenueService;
import com.tae.Etickette.venue.command.domain.DeleteVenuePolicy;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.command.domain.VenueStatus;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
    private final DeleteVenuePolicy deleteVenuePolicy = mock(DeleteVenuePolicy.class);

    @Test
    @DisplayName("delete - 공연장 삭제에 성공한다.")
    void 삭제_성공() {
        //given
        Venue venue = Venue.create("KSPO", 15000, new Address("서울시", "송파구 올림픽로 424", "11111"));
        ReflectionTestUtils.setField(venue,"id",1L);

        BDDMockito.given(venueRepository.findById(any()))
                .willReturn(Optional.of(venue));
        BDDMockito.given(deleteVenuePolicy.hasDeletePermission()).willReturn(true);

        //when
        deleteVenueService.delete(1L);

        //then
        Assertions.assertThat(venue.getStatus()).isEqualTo(VenueStatus.DELETED);
    }

    @Test
    @DisplayName("delete - 공연장이 없다면, 공연장 삭제에 실패한다.")
    void 삭제_실패_공연장이없음() {
        //given
        BDDMockito.given(venueRepository.findById(any()))
                .willReturn(Optional.empty());

        //when & then
        assertThrows(ResourceNotFoundException.class, () ->
                deleteVenueService.delete(any()));
    }

    @Test
    @DisplayName("delete - 권한이 없다면, 공연장 삭제에 실패한다.")
    void 삭제_실패_권한없음() {
        //given
        BDDMockito.given(venueRepository.findById(any()))
                .willReturn(Optional.of(mock(Venue.class)));
        BDDMockito.given(deleteVenuePolicy.hasDeletePermission()).willReturn(false);

        //when & then
        assertThrows(ForbiddenException
                .class, () ->
                deleteVenueService.delete(any()));
    }

}