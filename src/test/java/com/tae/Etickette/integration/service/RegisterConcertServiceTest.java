package com.tae.Etickette.integration.service;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.Concert;
import com.tae.Etickette.concert.infra.ConcertRepository;
import com.tae.Etickette.seat.infra.SeatRepository;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@DisplayName("Integration - RegisterConcertService")
public class RegisterConcertServiceTest {

    @Autowired
    RegisterConcertService registerConcertService;
    @Autowired
    ConcertRepository concertRepository;
    @Autowired
    SeatRepository seatRepository;

    @Test
    @DisplayName("공연 등록에 성공한다.")
    void 공연장생성_성공() {
        //given
        RegisterConcertRequest request = ConcertCreateBuilder.builder()
                .title("공연A")
                .overview("공연A입니다.")
                .runningTime(120).build();

        //when
        RegisterConcertResponse response = registerConcertService.register(request);

        //then
        Concert concert = concertRepository.findById(response.getConcertId()).get();
        Assertions.assertThat(concert.getTitle()).isEqualTo("공연A");
    }

    @Test
    @DisplayName("register - 공연이 등록되면, 좌석 정보도 등록된다.")
    void 공연장생성_성공_좌석생성() {
        //given
        RegisterConcertRequest request = ConcertCreateBuilder.builder().build();

        //when
        RegisterConcertResponse response = registerConcertService.register(request);

        //then
        List<Long> seatIds = seatRepository.findIdByConcertId(response.getConcertId());
        Assertions.assertThat(seatIds).isNotEmpty();
    }

}
