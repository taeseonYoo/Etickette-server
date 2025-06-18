package com.tae.Etickette.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.command.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.domain.Venue;
import com.tae.Etickette.venue.command.domain.VenueStatus;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VenueControllerTest {
    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void 공연장삭제_성공() throws Exception {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "잠실", "11111"));
        Venue save = venueRepository.save(venue);

        //when & then
        mockMvc.perform(delete("/api/venues/" + save.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());


        Venue findVenue = venueRepository.findById(venue.getId()).get();
        Assertions.assertThat(findVenue.getStatus()).isEqualTo(VenueStatus.DELETED);
    }

    @Test
    @WithMockUser(roles = "USER")
    void 공연장삭제_실패_권한이없음() throws Exception {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "잠실", "11111"));
        Venue save = venueRepository.save(venue);

        //when & then
        mockMvc.perform(delete("/api/venues/" + save.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 주소변경_성공() throws Exception {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "잠실", "11111"));
        Venue save = venueRepository.save(venue);

        ChangeAddressRequest request = ChangeAddressRequest.builder().address(new Address("강원도", "강릉", "22222")).build();

        //when & then
        mockMvc.perform(put("/api/venues/" + save.getId() + "/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isOk());

        Venue findVenue = venueRepository.findById(venue.getId()).get();
        Assertions.assertThat(findVenue.getAddress().getCity()).isEqualTo("강원도");
    }

    @Test
    @WithMockUser(roles = "USER")
    void 주소변경_실패_권한() throws Exception {
        //given
        Venue venue = Venue.create("KSPO", 10000, new Address("서울", "잠실", "11111"));
        Venue save = venueRepository.save(venue);

        ChangeAddressRequest request = ChangeAddressRequest.builder().address(new Address("강원도", "강릉", "22222")).build();

        //when & then
        mockMvc.perform(put("/api/venues/" + save.getId() + "/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void 공연장생성_성공() throws Exception {
        //given
        RegisterVenueRequest request = RegisterVenueRequest.builder()
                .place("KSPO")
                .capacity(10000)
                .address(new Address("서울", "잠실", "11111"))
                .build();
        //when
        mockMvc.perform(post("/api/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void 공연장생성_실패_권한() throws Exception {
        //given
        RegisterVenueRequest request = RegisterVenueRequest.builder()
                .place("KSPO")
                .capacity(10000)
                .address(new Address("서울", "잠실", "11111"))
                .build();
        //when
        mockMvc.perform(post("/api/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("공연장 검색에 성공한다.")
    void 공연장검색_성공() throws Exception {
        //given
        Venue venue1 = Venue.create("KSPO DOME", 10000, new Address("서울", "올림픽로", "12345"));
        Venue venue2 = Venue.create("올림픽홀", 5000, new Address("서울", "잠실로", "54321"));
        venueRepository.saveAll(List.of(venue1, venue2));

        //when & then
        mockMvc.perform(get("/api/venues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("공연장 검색 , 삭제된 공연장은 제외해야한다.")
    void 공연장검색_성공_삭제된공연장은제외() throws Exception {
        //given
        Venue venue1 = Venue.create("KSPO DOME", 10000, new Address("서울", "올림픽로", "12345"));
        Venue venue2 = Venue.create("올림픽홀", 5000, new Address("서울", "잠실로", "54321"));
        venueRepository.saveAll(List.of(venue1, venue2));
        venue2.deleteVenue();

        //when & then
        mockMvc.perform(get("/api/venues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("공연장 검색에 성공하면, [id,장소명,관객수,주소]가 제대로 반환되어야한다.")
    void 공연장검색_성공_데이터검증() throws Exception {
        //given
        Venue venue1 = Venue.create("KSPO DOME", 10000, new Address("서울", "올림픽로", "12345"));
        Venue venue2 = Venue.create("올림픽홀", 5000, new Address("서울", "잠실로", "54321"));
        venueRepository.saveAll(List.of(venue1, venue2));
        venue2.deleteVenue();

        //when & then
        mockMvc.perform(get("/api/venues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].place").value("KSPO DOME"))
                .andExpect(jsonPath("$[0].capacity").value(10000))
                .andExpect(jsonPath("$[0].address.city").value("서울"))
                .andExpect(jsonPath("$[0].address.street").value("올림픽로"))
                .andExpect(jsonPath("$[0].address.zipcode").value("12345"));
    }

    @Test
    @DisplayName("공연장 목록 불러오기에 성공하면, 활성중인 데이터만 출력되어야한다.")
    void 공연장목록_성공_ACTIVE() throws Exception {
        //given
        Venue venue1 = Venue.create("KSPO DOME", 10000, new Address("서울", "올림픽로", "12345"));
        Venue venue2 = Venue.create("올림픽홀", 5000, new Address("서울", "잠실로", "54321"));
        venueRepository.saveAll(List.of(venue1, venue2));
        venue2.deleteVenue();

        //when & then
        mockMvc.perform(get("/api/venues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
