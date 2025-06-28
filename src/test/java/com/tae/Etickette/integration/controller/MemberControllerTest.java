package com.tae.Etickette.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.member.application.MemberService;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import org.junit.jupiter.api.DisplayName;
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
public class MemberControllerTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("회원가입에 성공하면, 201 상태 코드를 반환한다.")
    void 회원가입_성공() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();

        //when & then
        mockMvc.perform(post("/api/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("비밀번호 수정에 성공하면, 204 상태 코드를 반환한다.")
    @WithMockUser(username = "test@spring")
    void 비밀번호수정_성공() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        ChangePasswordRequest passwordRequest = ChangePasswordRequest.builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123")
                .email("test@spring")
                .build();

        //when & then
        mockMvc.perform(put("/api/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("어드민은 비밀번호를 수정할 수 있다.")
    @WithMockUser(roles = "ADMIN",username = "admin@spring")
    void 비밀번호수정_성공_어드민() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        ChangePasswordRequest passwordRequest = ChangePasswordRequest.builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123")
                .email("test@spring")
                .build();

        //when & then
        mockMvc.perform(put("/api/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("본인이 아닌 경우, 정보를 수정할 수 없다.")
    @WithMockUser(username = "hacker@spring")
    void 비밀번호수정_실패_권한없음() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        ChangePasswordRequest passwordRequest = ChangePasswordRequest.builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123")
                .email("test@spring")
                .build();

        //when & then
        mockMvc.perform(put("/api/members/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(passwordRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("회원 삭제에 성공하면, 204 상태 코드를 반환한다.")
    @WithMockUser(username = "test@spring")
    void 회원삭제_성공() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        DeleteMemberRequest deleteRequest = DeleteMemberRequest.builder().email("test@spring").build();

        //when & then
        mockMvc.perform(delete("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("어드민은 회원을 삭제할 수 있다.")
    @WithMockUser(roles = "ADMIN")
    void 회원삭제_성공_어드민() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        DeleteMemberRequest deleteRequest = DeleteMemberRequest.builder().email("test@spring").build();

        //when & then
        mockMvc.perform(delete("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("본인이 아닌 경우, 삭제에 실패한다.")
    @WithMockUser(username = "hacker@spring")
    void 회원삭제_실패_권한없음() throws Exception {
        //given
        RegisterMemberRequest request = RegisterMemberRequest.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc1234")
                .build();
        memberService.register(request);

        DeleteMemberRequest deleteRequest = DeleteMemberRequest.builder().email("test@spring").build();

        //when & then
        mockMvc.perform(delete("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(deleteRequest)))
                .andExpect(status().isInternalServerError());
    }
}
