package com.tae.Etickette.integration.controller;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.SessionCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SessionControllerTest {

    @Autowired
    RegisterSessionService registerSessionService;
    @Autowired
    RegisterConcertService registerConcertService;
    @Autowired
    RegisterVenueService registerVenueService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private JacksonTester<RegisterSessionRequest> json;

    @Test
    void 세션등록_성공() throws Exception {
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        Long concertId = registerConcertService.register(concertRequest);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder()
                .concertId(concertId)
                .build();

        //when
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.write(sessionRequest).getJson())
        ).andExpect(status().isOk());
    }
}
