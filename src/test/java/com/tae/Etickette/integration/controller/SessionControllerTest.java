package com.tae.Etickette.integration.controller;

import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.session.domain.SessionCanceledEvent;
import com.tae.Etickette.session.domain.SessionStatus;
import com.tae.Etickette.session.infra.SessionRepository;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.SessionCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    SessionRepository sessionRepository;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ImageUploader imageUploader;
    @Autowired
    private JacksonTester<RegisterSessionRequest> json;

    @Test
    @WithMockUser
    void 세션등록_성공() throws Exception {
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        BDDMockito.given(imageUploader.upload(multipartFile)).willReturn("amazonaws.com");
        RegisterConcertResponse response = registerConcertService.register(concertRequest,multipartFile);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder()
                .concertId(response.getConcertId())
                .build();

        //when
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.write(sessionRequest).getJson())
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 세션취소_성공() throws Exception {
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "testImage.png",
                "image/png",
                "이미지데이터".getBytes()
        );
        BDDMockito.given(imageUploader.upload(multipartFile)).willReturn("amazonaws.com");
        RegisterConcertResponse response = registerConcertService.register(concertRequest,multipartFile);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder()
                .concertId(response.getConcertId())
                .build();
        List<Long> sessionIds = registerSessionService.register(sessionRequest);

        //when
        mockMvc.perform(post("/api/sessions/" + sessionIds.get(0) + "/cancel")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
