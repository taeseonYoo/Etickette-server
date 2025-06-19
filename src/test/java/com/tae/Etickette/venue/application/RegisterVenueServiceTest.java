package com.tae.Etickette.venue.application;

import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - RegisterVenueService")
class RegisterVenueServiceTest {
    @InjectMocks
    private RegisterVenueService registerVenueService;
    private final VenueRepository mockVenueRepo = mock(VenueRepository.class);

    @BeforeEach
    public void setup() {
        registerVenueService = new RegisterVenueService(mockVenueRepo);
    }

    @Test
    @DisplayName("register - 공연장 등록에 성공한다.")
    public void 공연장등록_성공() {
        //given
        RegisterVenueRequest requestDto = builder()
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
        RegisterVenueResponse responseDto = registerVenueService.register(requestDto);

        //then
        assertThat(responseDto.getPlace()).isEqualTo("KSPO DOME");
    }
    @Test
    @DisplayName("register - 주소가 존재하면, 공연장 등록에 실패한다.")
    public void 공연장등록_실패_주소가존재함() {
        //given
        RegisterVenueRequest requestDto = builder()
                .place("KSPO DOME")
                .capacity(10000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();

        BDDMockito.given(mockVenueRepo.findVenueByAddress(any()))
                .willReturn(Optional.of(mock(Venue.class)));

        //when & then
        Assertions.assertThrows(DuplicateKeyException.class,
                () -> registerVenueService.register(requestDto));
    }

}