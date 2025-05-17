package com.tae.Etickette.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @AutoConfigureMockMvc(addFilters = false) Spring Security 의 자동 구성을 비활성화한다.
 */

@DisplayName("Unit - MemberController")
@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    MemberService memberService;

    @Test
    @DisplayName("join - 회원 가입에 성공한다.")
    void 회원가입_성공() throws Exception {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234").build();

        MemberJoinResponseDto responseDto = MemberJoinResponseDto.builder()
                .id(1L)
                .name("test")
                .email("test@spring").build();

        BDDMockito.given(memberService.join(any())).willReturn(responseDto);

        //when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@spring"));
    }

    @Test
    @DisplayName("join - 이메일이 중복이라면, 회원가입에 실패하고 500을 반환한다.")
    void 회원가입_실패_이메일중복() throws Exception {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234").build();


        BDDMockito.given(memberService.join(any())).willThrow(DuplicateKeyException.class);

        //when & then
        mockMvc.perform(post("/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isInternalServerError());
    }




}