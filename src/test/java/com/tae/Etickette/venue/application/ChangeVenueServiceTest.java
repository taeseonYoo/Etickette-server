package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.venue.command.application.ChangeVenueService;
import com.tae.Etickette.venue.command.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - ChangeVenueService")
class ChangeVenueServiceTest {

    @InjectMocks
    private ChangeVenueService changeVenueService;
    private final VenueRepository venueRepository = mock(VenueRepository.class);


    @Test
    @DisplayName("changeAddress - 공연장 주소 변경에 성공한다.")
    void 주소변경_성공() {
        //given
        Venue venue = Venue.create("KSPO", 15000, new Address("서울시", "송파구 올림픽로 424", "11111"));

        Address newAddress = new Address("서울시", "관악구 난곡로 242", "22222");
        ChangeAddressRequest requestDto = ChangeAddressRequest.builder()
                .address(newAddress)
                .build();

        BDDMockito.given(venueRepository.findById(any())).willReturn(Optional.of(venue));
        BDDMockito.given(venueRepository.findVenueByAddress(any())).willReturn(Optional.empty());

        //when
        changeVenueService.changeAddress(1L,requestDto);

        //then
        assertThat(venue.getAddress()).isEqualTo(newAddress);
    }

    @Test
    @DisplayName("changeAddress - 공연장이 없다면, 공연장 주소 변경에 실패한다.")
    void 주소변경_실패_공연장없음() {
        //given
        ChangeAddressRequest requestDto = ChangeAddressRequest.builder()
                .address(new Address("서울시", "관악구 난곡로 242", "22222"))
                .build();

        BDDMockito.given(venueRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                changeVenueService.changeAddress(1L,requestDto));
    }

    @Test
    @DisplayName("changeAddress - 중복된 주소를 사용하는 공연장이 있다면, 공연장 주소 변경에 실패한다.")
    void 주소변경_실패_주소가이미존재함() {
        //given
        ChangeAddressRequest requestDto = ChangeAddressRequest.builder()
                .address(new Address("서울시", "관악구 난곡로 242", "22222"))
                .build();

        BDDMockito.given(venueRepository.findById(any())).willReturn(Optional.of(mock(Venue.class)));
        BDDMockito.given(venueRepository.findVenueByAddress(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(BadRequestException.class, () ->
                changeVenueService.changeAddress(1L,requestDto));
    }
}