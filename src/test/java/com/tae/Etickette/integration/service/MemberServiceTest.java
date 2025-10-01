package com.tae.Etickette.integration.service;

import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.refresh.domain.RefreshToken;
import com.tae.Etickette.global.refresh.infra.RefreshTokenRepository;
import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.domain.EncryptionService;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.MemberStatus;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.member.application.MemberService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("join - 회원 가입에 성공하면, 회원 정보가 저장된다.")
    public void 회원가입_성공() {
        //given
        RegisterMemberRequest member = RegisterMemberRequest.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();

        //when
        RegisterMemberResponse savedMember = memberService.register(member);

        //then
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());
        Assertions.assertThat(findMember.isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("changePassword - 비밀번호 변경에 성공하면, 변경된 비밀번호가 저장된다.")
    public void 비밀번호변경_성공() {
        //given
        RegisterMemberRequest member = RegisterMemberRequest.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.register(member);

        ChangePasswordRequest requestDto = ChangePasswordRequest
                .builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123")
                .email("USER@spring").build();

        //when
        memberService.changePassword(requestDto,"USER@spring");

        //then
        Member findMember = memberRepository.findByEmail("USER@spring").get();
        Assertions.assertThat(encryptionService.matches(
                "@Change123",
                findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("changePassword - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 비밀번호변경_실패_비밀번호불일치() {
        //given
        RegisterMemberRequest member = RegisterMemberRequest.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.register(member);

        ChangePasswordRequest requestDto = ChangePasswordRequest.builder()
                .oldPassword("@Bad1234")
                .newPassword("@Change123")
                .email("USER@spring")
                .build();

        //when & then
        assertThrows(BadRequestException.class,
                () -> memberService.changePassword(requestDto, "USER@spring"));
    }

    @Test
    @DisplayName("deleteMember - 회원 삭제에 성공하면, status = DELETE")
    public void 회원삭제_성공() {
        //given
        RegisterMemberRequest member = RegisterMemberRequest.builder()
                .name("USER")
                .email("USER@spring").password("@Abc1234")
                .build();
        memberService.register(member);

        DeleteMemberRequest request = DeleteMemberRequest.builder().email("USER@spring").build();

        //when
        memberService.deleteMember(request,"USER@spring");

        //then
        Member findMember = memberRepository.findByEmail("USER@spring").get();
        Assertions.assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.DELETE);
    }
    @Test
    @DisplayName("deleteMember - 회원 삭제에 성공하면, 리프레시 토큰이 삭제된다.")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void 회원삭제_성공_이벤트() {
        //given
        refreshTokenRepository.save(new RefreshToken("event@spring", "refresh.token.event", "expiration"));

        RegisterMemberRequest member = RegisterMemberRequest.builder()
                .name("USER")
                .email("event@spring").password("@Abc1234")
                .build();
        memberService.register(member);

        DeleteMemberRequest request = DeleteMemberRequest.builder().email("event@spring").build();

        //when
        memberService.deleteMember(request,"event@spring");
        refreshTokenRepository.flush();

        //then
        Assertions.assertThat(refreshTokenRepository.existsByRefresh("refresh.token.event")).isFalse();
    }
}
