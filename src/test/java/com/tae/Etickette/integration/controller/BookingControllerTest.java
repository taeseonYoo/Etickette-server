package com.tae.Etickette.integration.controller;

import com.tae.Etickette.booking.application.BookingService;
import com.tae.Etickette.booking.application.CancelBookingService;
import com.tae.Etickette.booking.application.dto.BookingRequest;
import com.tae.Etickette.booking.domain.BookingRef;
import com.tae.Etickette.concert.command.application.RegisterConcertService;
import com.tae.Etickette.concert.command.application.dto.RegisterConcertRequest;
import com.tae.Etickette.global.auth.CustomUserDetails;
import com.tae.Etickette.member.application.MemberService;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import com.tae.Etickette.session.domain.Session;
import com.tae.Etickette.testhelper.ConcertCreateBuilder;
import com.tae.Etickette.testhelper.SessionCreateBuilder;
import com.tae.Etickette.testhelper.VenueCreateBuilder;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueRequest;
import com.tae.Etickette.venue.command.application.Dto.RegisterVenueResponse;
import com.tae.Etickette.venue.command.application.RegisterVenueService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingControllerTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    CancelBookingService cancelBookingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    RegisterVenueService registerVenueService;
    @Autowired
    RegisterConcertService registerConcertService;
    @Autowired
    RegisterSessionService registerSessionService;
    @Autowired
    MemberRepository memberRepository;

    Member savedMember;
    @BeforeEach
    void setUp() {
        Member member = Member.create("user", "user@naver", "@Abc", Role.ADMIN);
        savedMember = memberRepository.save(member);
        CustomUserDetails userDetails = new CustomUserDetails(savedMember);
        ReflectionTestUtils.setField(savedMember,"id",1L);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void afterEach() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 예약취소() throws Exception {
        //given
        RegisterVenueRequest venueRequest = VenueCreateBuilder.builder().build();
        RegisterVenueResponse venueResponse = registerVenueService.register(venueRequest);

        RegisterConcertRequest concertRequest = ConcertCreateBuilder.builder().venueId(venueResponse.getId()).build();
        Long concertId = registerConcertService.register(concertRequest);

        RegisterSessionRequest sessionRequest = SessionCreateBuilder.builder().concertId(concertId).sessionInfos(List.of(RegisterSessionRequest.SessionInfo.builder().concertDate(LocalDate.of(2025, 6, 6)).startTime(LocalTime.of(15, 0)).build())).build();
        List<Long> sessions = registerSessionService.register(sessionRequest);

        BookingRequest request = BookingRequest.builder().memberId(savedMember.getId()).sessionId(sessions.get(0)).seatIds(List.of(1L)).build();
        BookingRef bookingRef = bookingService.booking(request);

        //when
        mockMvc.perform(post("/api/booking/"+bookingRef.getValue()+"/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}