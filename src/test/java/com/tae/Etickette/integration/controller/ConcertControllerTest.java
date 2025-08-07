package com.tae.Etickette.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertResponse;
import com.tae.Etickette.concert.command.domain.ImageUploader;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    EntityManager entityManager;
    //이미지 업로드가 실제로 일어나지 않도록 한다.
    @MockitoBean
    ImageUploader imageUploader;

    MockMultipartFile multipartFile;
    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile("image", "testImage.png", "image/png", "이미지데이터".getBytes());
        BDDMockito.given(imageUploader.upload(multipartFile)).willReturn("amazonaws.com");
        //공연 요약 정보의 캐시를 삭제한다.
        cacheManager.getCache("concerts").clear();
    }

    @Test
    @DisplayName("공연 요약 조회에 성공한다.")
    void 공연요약조회_성공() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().build());
        RegisterConcertResponse response1 = concertService.register(ConcertCreateBuilder.builder().title("공연 A").venueId(venue.getId()).build(),multipartFile);
        RegisterConcertResponse response2 = concertService.register(ConcertCreateBuilder.builder().title("공연 B").venueId(venue.getId()).build(),multipartFile);

        //when & then
        mockMvc.perform(get("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("공연 요약 조회에 성공하면, 값(concertId,title,imgURL,place)검증에 성공한다.")
    void 공연요약조회_성공_값검증() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().place("KSPO").build());

        RegisterConcertResponse response = concertService.register(ConcertCreateBuilder.builder().title("공연 C").venueId(venue.getId()).build(), multipartFile);

        //when & then
        mockMvc.perform(get("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].concertId").value(response.getConcertId()))
                .andExpect(jsonPath("$[0].title").value("공연 C"))
                .andExpect(jsonPath("$[0].image_path").value(Matchers.containsString("amazonaws.com")))
                .andExpect(jsonPath("$[0].place").value("KSPO"))
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

        RegisterConcertResponse response = concertService.register(ConcertCreateBuilder.builder().title("공연 D").venueId(venue.getId()).build(),multipartFile);

        //when & then
        mockMvc.perform(get("/api/concerts/" + response.getConcertId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("공연 등록 - 공연 등록 성공")
    void 공연등록_성공() throws Exception {
        //given
        RegisterVenueResponse venue = venueService.register(VenueCreateBuilder.builder().place("KSPO").build());

        RegisterConcertRequest request = ConcertCreateBuilder.builder().venueId(venue.getId()).build();
        String json = new ObjectMapper().writeValueAsString(request);

        MockMultipartFile concertRequestFile = new MockMultipartFile("request", "", "application/json", json.getBytes(StandardCharsets.UTF_8));

        //when & then
        mockMvc.perform(multipart("/api/concerts")
                        .file(multipartFile)
                        .file(concertRequestFile))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
