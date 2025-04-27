package com.tae.Etickette.unit.service;

import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.repository.MemberRepository;
import com.tae.Etickette.member.service.DuplicateEmailException;
import com.tae.Etickette.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("Unit - MemberService")
public class MemberServiceTest {

    private MemberService memberService;
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(mockMemberRepo);
    }

    @Test
    @DisplayName("회원 가입 - 회원 가입에 성공한다.")
    public void 회원_가입() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("tester")
                .email("test@spring.io")
                .password("12345678").build();
        Member member = requestDto.toEntity();
        ReflectionTestUtils.setField(member, "id",1L);

        BDDMockito.given(mockMemberRepo.findByEmail(any()))
                .willReturn(Optional.empty());
        BDDMockito.given(mockMemberRepo.save(any()))
                .willReturn(member);

        //when
        MemberJoinResponseDto responseDto = memberService.join(requestDto);

        //then
        Assertions.assertThat(member.getId()).isEqualTo(responseDto.getId());
    }

    @Test
    @DisplayName("회원 가입 - 중복된 이메일이 있으면, 회원 가입에 실패한다.")
    public void 회원_가입_실패() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("tester")
                .email("test@spring.io")
                .password("12345678").build();
        Member member = requestDto.toEntity();
        ReflectionTestUtils.setField(member, "id",1L);

        BDDMockito.given(mockMemberRepo.findByEmail(any()))
                .willReturn(Optional.of(member));

        //when
        assertThrows(DuplicateEmailException.class,
                ()->memberService.join(requestDto));
    }



}
