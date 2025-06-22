package com.tae.Etickette.integration.controller;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.SessionCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.tae.Etickette.session.application.Dto.RegisterSessionRequest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ConcertControllerTest {
    @Autowired
    RegisterConcertService concertService;
    @Autowired
    RegisterSessionService sessionService;
    @Autowired
    RegisterVenueService venueService;
    @Autowired
    MockMvc mockMvc;



    @Test
    @DisplayName("공연 요약 조회에 성공한다.")
    void 공연요약조회_성공() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().build());

        Long savedConcert1 = concertService.register(ConcertCreateBuilder.builder().title("공연 A").venueId(venue.getId()).build());
        Long savedConcert2 = concertService.register(ConcertCreateBuilder.builder().title("공연 B").venueId(venue.getId()).build());

        sessionService.register(SessionCreateBuilder.builder().concertId(savedConcert1).build());
        sessionService.register(SessionCreateBuilder.builder().concertId(savedConcert2)
                .sessionInfos(List.of(SessionInfo.builder().concertDate(LocalDate.of(2025, 5, 10)).startTime(LocalTime.of(15, 0)).build())).build());

        //when & then
        mockMvc.perform(get("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("공연 요약 조회에 성공하면, 값(concertId,title,imgURL,place)검증에 성공한다.")
    void 공연요약조회_성공_값검증() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().place("KSPO").build());

        Long concertId = concertService.register(ConcertCreateBuilder.builder().title("공연 A").imgURL("").venueId(venue.getId()).build());

        sessionService.register(SessionCreateBuilder.builder().concertId(concertId).build());

        //when & then
        mockMvc.perform(get("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].concertId").value(concertId))
                .andExpect(jsonPath("$.content[0].title").value("공연 A"))
                .andExpect(jsonPath("$.content[0].imgURL").value(""))
                .andExpect(jsonPath("$.content[0].place").value("KSPO"))
        ;
    }

    @Test
    @DisplayName("공연 상세 정보 - Optional 이 반환되면, 응답코드 == NOTFOUND")
    void 공연상세정보_NOTFOUND() throws Exception {
        //given
        RegisterVenueResponse lastVenue = venueService.register(VenueCreateBuilder.builder().build());

        //when & then
        mockMvc.perform(get("/api/concerts/" + lastVenue.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공연 상세 정보 - 데이터가 반환되면, 응답코드 == OK")
    void 공연상세정보_OK() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().place("KSPO").build());

        Long concertId = concertService.register(ConcertCreateBuilder.builder().title("공연 A").imgURL("").venueId(venue.getId()).build());

        sessionService.register(SessionCreateBuilder.builder().concertId(concertId).build());

        //when & then
        mockMvc.perform(get("/api/concerts/" + concertId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
