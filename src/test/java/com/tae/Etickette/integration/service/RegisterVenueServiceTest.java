package com.tae.Etickette.integration.service;

import com.tae.Etickette.member.application.AlreadyExisingEmailException;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import com.tae.Etickette.concert.command.domain.Address;
import com.tae.Etickette.venue.command.application.VenueNotFoundException;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest.builder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Integration - Venue")
public class RegisterVenueServiceTest {
    @Autowired
    private RegisterVenueService registerVenueService;
    @Autowired
    private VenueRepository venueRepository;

    @Test
    @DisplayName("register - 공연장 등록에 성공한다.")
    public void 공연장등록_성공() {
        //given
        RegisterVenueRequest requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        //when
        RegisterVenueResponse responseDto = registerVenueService.register(requestDto);

        //then
        boolean exist = venueRepository.findById(responseDto.getId()).isPresent();
        Assertions.assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("register - 주소가 이미 등록되어 있다면, 공연장 등록에 실패한다.")
    public void 공연장등록_실패_중복된주소() {
        //given
        venueRepository.save(Venue.create("KSPO", 15000, new Address("서울시", "송파구 올림픽로 424", "11111")));

        RegisterVenueRequest requestDto = builder()
                .place("KSPO DOME(올림픽 체조경기장)")
                .capacity(15000)
                .address(new Address("서울시", "송파구 올림픽로 424", "11111"))
                .build();
        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> registerVenueService.register(requestDto));
    }
}
