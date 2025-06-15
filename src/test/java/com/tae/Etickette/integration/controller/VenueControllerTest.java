package com.tae.Etickette.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.concert.domain.Address;
import com.tae.Etickette.venue.application.Dto.ChangeAddressRequest;
import com.tae.Etickette.venue.application.Dto.VenueRegisterRequest;
import com.tae.Etickette.venue.domain.Venue;
import com.tae.Etickette.venue.domain.VenueStatus;
import com.tae.Etickette.venue.infra.VenueRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        VenueRegisterRequest request = VenueRegisterRequest.builder()
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
        VenueRegisterRequest request = VenueRegisterRequest.builder()
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
}
