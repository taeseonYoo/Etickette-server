package com.tae.Etickette.member.application;

import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.global.exception.ResourceNotFoundException;
import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.domain.*;
import com.tae.Etickette.member.infra.EncryptionServiceImpl;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.infra.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit - MemberService")
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final EncryptionService encryptionService = mock(EncryptionServiceImpl.class);
    private final ChangePolicy changePolicy = mock(ChangePolicy.class);


    @Test
    @DisplayName("join - 회원 가입에 성공한다.")
    public void 회원가입_성공() {
        //given
        RegisterMemberRequest requestDto = RegisterMemberRequest.builder()
                .name("사용자")
                .email("USER@spring")
                .password("@Abc1234").build();

        Member member = Member.create(requestDto.getName(), requestDto.getEmail(), encryptionService.encode(requestDto.getPassword()), Role.USER);
        ReflectionTestUtils.setField(member, "id", 1L);

        BDDMockito.given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());
        BDDMockito.given(memberRepository.save(any()))
                .willReturn(member);

        //when
        RegisterMemberResponse responseDto = memberService.register(requestDto);

        //then
        Assertions.assertThat(member.getId()).isEqualTo(responseDto.getId());
    }

    @Test
    @DisplayName("join - 중복된 이메일이 존재하면, 회원 가입에 실패한다.")
    public void 회원가입_실패_중복이메일() {
        //given
        RegisterMemberRequest requestDto = RegisterMemberRequest.builder()
                .name("사용자")
                .email("USER@spring")
                .password("@Abc1234").build();

        BDDMockito.given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(mock(Member.class)));

        //when & then
        assertThrows(BadRequestException.class,
                () -> memberService.register(requestDto));
    }
    @Test
    @DisplayName("join - 비밀번호가 누락되면, 회원 가입에 실패한다.")
    public void 회원가입_실패_비밀번호누락() {
        //given
        RegisterMemberRequest requestDto = RegisterMemberRequest.builder()
                .name("사용자")
                .email("USER@spring")
                .build();

        BDDMockito.given(memberRepository.save(any())).willThrow(IllegalArgumentException.class);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> memberService.register(requestDto));
    }

    @Test
    @DisplayName("join - 이름이 누락되면, 회원 가입에 실패한다.")
    public void 회원가입_실패_이름누락() {
        //given
        RegisterMemberRequest requestDto = RegisterMemberRequest.builder()
                .email("USER@spring")
                .password("@Abc1234")
                .build();

        BDDMockito.given(memberRepository.save(any())).willThrow(IllegalArgumentException.class);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> memberService.register(requestDto));
    }

    @Test
    @DisplayName("changePassword - 비밀번호 변경에 성공한다.")
    public void 비밀번호변경_성공() {
        //given
        Member member = Member.create("사용자", "USER@spring", "@Abc1234", Role.USER);
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123")
                .email("USER@spring").build();

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(true);
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(true);

        //when
        memberService.changePassword(requestDto,"USER@spring");

        //then
        Member findMember = memberRepository.findByEmail("USER@spring").get();
        Assertions.assertThat(encryptionService.matches("@Change123", findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("changePassword - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 비밀번호변경_실패_비밀번호불일치() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder().email("USER@spring").build();

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(false);
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(true);

        //when & then
        assertThrows(BadRequestException.class,
                () -> memberService.changePassword(requestDto,"USER@spring")
        );
    }

    @Test
    @DisplayName("changePassword - 회원이 존재하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 비밀번호변경_실패_회원없음() {
        //given
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder().email("USER@spring").build();

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(ResourceNotFoundException.class,
                () -> memberService.changePassword(requestDto, "USER@spring")
        );
    }
    @Test
    @DisplayName("changePassword - 삭제할 회원과 요청 회원이 다르다면, 비밀번호 변경에 실패한다.")
    public void 회원정보변경_실패_권한없음() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);
        ChangePasswordRequest requestDto = ChangePasswordRequest.builder().email("USER@spring").build();

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(false);

        //when & then
        assertThrows(ForbiddenException.class,
                () -> memberService.changePassword(requestDto, "hacker@spring")
        );
    }

    @Test
    @DisplayName("deleteMember - 회원 삭제에 성공한다.")
    public void 회원삭제_성공() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(true);

        DeleteMemberRequest request = DeleteMemberRequest.builder().email("USER@spring").build();

        //when
        memberService.deleteMember(request,"USER@spring");

        //then
        Assertions.assertThat(member.getMemberStatus())
                .isEqualTo(MemberStatus.DELETE);
    }

    @Test
    @DisplayName("deleteMember - 삭제하려는 회원 정보가 없다면, 회원 삭제에 실패한다.")
    public void 회원삭제_실패_회원이없음() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.empty());
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(true);

        DeleteMemberRequest request = DeleteMemberRequest.builder().email("USER@spring").build();

        //when
        assertThrows(ResourceNotFoundException.class,
                ()->memberService.deleteMember(request, "USER@spring"));
    }

    @Test
    @DisplayName("deleteMember - 삭제 권한이 없으면, 회원 삭제에 실패한다.")
    public void 회원삭제_실패_권한없음() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(changePolicy.hasUpdatePermission(any(), any())).willReturn(false);

        DeleteMemberRequest request = DeleteMemberRequest.builder().email("USER@spring").build();

        //when & then
        assertThrows(ForbiddenException.class,
                () -> memberService.deleteMember(request,"hacker@spring"));
    }

}
