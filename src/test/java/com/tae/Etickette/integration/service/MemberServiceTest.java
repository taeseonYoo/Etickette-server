package com.tae.Etickette.integration.service;

import com.tae.Etickette.global.auth.EncryptionService;
import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.dto.PasswordChangeRequestDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.entity.MemberStatus;
import com.tae.Etickette.member.repository.MemberRepository;
import com.tae.Etickette.member.service.BadPasswordException;
import com.tae.Etickette.member.service.DuplicateEmailException;
import com.tae.Etickette.member.service.MemberDeleteRequestDto;
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
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EncryptionService encryptionService;

    @Test
    @DisplayName("join - 회원 가입에 성공한다.")
    public void 회원가입_성공() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();

        //when
        MemberJoinResponseDto savedMember = memberService.join(member);

        //then
        Member findMember = memberService.findById(savedMember.getId());
        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @DisplayName("join - 중복된 이메일이 있으면, 회원 가입에 실패한다.")
    public void 회원가입_실패_중복이메일() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.join(member);
        MemberJoinRequestDto member2 = MemberJoinRequestDto.builder()
                .name("USER2")
                .email("USER@spring").password("@Abc1234")
                .build();

        //then
        assertThrows(DuplicateEmailException.class,
                () -> memberService.join(member2));
    }

    @Test
    @DisplayName("changePassword - 비밀번호 변경에 성공한다.")
    public void 비밀번호변경_성공() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.join(member);

        PasswordChangeRequestDto requestDto = PasswordChangeRequestDto.builder()
                .oldPassword("@Abc1234").newPassword("@Change123").build();


        //when
        memberService.changePassword(requestDto, "USER@spring");

        //then
        Member findMember = memberRepository.findByEmail("USER@spring").get();
        Assertions.assertThat(encryptionService.matches("@Change123", findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("changePassword - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 비밀번호변경_실패_비밀번호불일치() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.join(member);

        PasswordChangeRequestDto requestDto = PasswordChangeRequestDto.builder()
                .oldPassword("@Bad1234").newPassword("@Change123").build();


        //when
        assertThrows(BadPasswordException.class,
                () -> memberService.changePassword(requestDto, "USER@spring"));

    }

    @Test
    @DisplayName("deleteMember - 회원 삭제에 성공한다.")
    public void 회원삭제_성공() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.join(member);

        MemberDeleteRequestDto requestDto = MemberDeleteRequestDto.builder()
                .password("@Abc1234").build();
        //when
        memberService.deleteMember(requestDto, "USER@spring");

        //then
        Member findMember = memberService.findByEmail("USER@spring");
        Assertions.assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.DELETE);
    }

    @Test
    @DisplayName("deleteMember - 기존 비밀번호가 일치하지 않으면, 회원 삭제에 실패한다.")
    public void 회원삭제_실패_비밀번호불일치() {
        //given
        MemberJoinRequestDto member = MemberJoinRequestDto.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.join(member);
        MemberDeleteRequestDto requestDto = MemberDeleteRequestDto.builder()
                .password("@Fake1234").build();
        //when & then
        assertThrows(RuntimeException.class,
                () -> memberService.deleteMember(requestDto, "USER@spring"));
    }
}
