package com.tae.Etickette.integration.service;

import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.service.DuplicateEmailException;
import com.tae.Etickette.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Integration - MemberService")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 - 회원 가입에 성공한다.")
    public void 회원_가입_성공() {
        //given
        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .name("tester")
                .email("test@spring.io")
                .password("12345678").build();

        //when
        MemberJoinResponseDto savedMember = memberService.join(memberJoinRequestDto);

        //then
        Member findMember = memberService.findById(savedMember.getId());
        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("회원 가입 - 중복된 이메일이 있으면, 회원 가입에 실패한다.")
    public void 회원_가입_실패() {
        //given
        MemberJoinRequestDto member1 = MemberJoinRequestDto.builder()
                .name("tester1")
                .email("test@spring.io").password("12345678")
                .build();
        memberService.join(member1);
        MemberJoinRequestDto member2 = MemberJoinRequestDto.builder()
                .name("tester2")
                .email("test@spring.io").password("12345678")
                .build();

        //then
        assertThrows(DuplicateEmailException.class,
                () -> memberService.join(member2));
    }

}
