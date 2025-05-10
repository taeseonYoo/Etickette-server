package com.tae.Etickette.unit.service;

import com.tae.Etickette.EncryptionService;
import com.tae.Etickette.EncryptionServiceImpl;
import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.dto.PasswordChangeRequestDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.entity.Role;
import com.tae.Etickette.member.repository.MemberRepository;
import com.tae.Etickette.member.service.BadPasswordException;
import com.tae.Etickette.member.service.DuplicateEmailException;
import com.tae.Etickette.member.service.MemberNotFoundException;
import com.tae.Etickette.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("Unit - MemberService")
public class MemberServiceTest {

    private MemberService memberService;
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);
    private final EncryptionService encryptionService = Mockito.mock(EncryptionServiceImpl.class);

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(mockMemberRepo, encryptionService);
    }

    @Test
    @DisplayName("회원 가입 - 회원 가입에 성공한다.")
    public void 회원_가입() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("사용자")
                .email("USER@spring")
                .password("@Abc1234").build();

        Member member = requestDto.toEntity(encryptionService,requestDto.getPassword());
        ReflectionTestUtils.setField(member, "id", 1L);

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
                .name("사용자")
                .email("USER@spring")
                .password("@Abc1234").build();

        Member member = requestDto.toEntity(encryptionService,requestDto.getPassword());
        ReflectionTestUtils.setField(member, "id", 1L);

        BDDMockito.given(mockMemberRepo.findByEmail(any()))
                .willReturn(Optional.of(member));

        //when
        assertThrows(DuplicateEmailException.class,
                () -> memberService.join(requestDto));
    }
    @Test
    @DisplayName("회원 가입 - 비밀번호가 누락되면, 회원 가입에 실패한다.")
    public void 회원_가입_실패_비밀번호_누락() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("사용자")
                .email("USER@spring")
                .build();

        Member member = requestDto.toEntity(encryptionService,requestDto.getPassword());
        ReflectionTestUtils.setField(member, "id", 1L);

        BDDMockito.given(mockMemberRepo.findByEmail(any()))
                .willReturn(Optional.empty());

        BDDMockito.given(mockMemberRepo.save(any())).willThrow(IllegalArgumentException.class);

        //when
        assertThrows(IllegalArgumentException.class,
                () -> memberService.join(requestDto));
    }

    @Test
    @DisplayName("회원 정보 변경 - 비밀번호 변경에 성공한다.")
    public void 회원_정보_변경_성공() {
        //given
        Member member = Member.create("사용자", "USER@spring", "@Abc1234", Role.USER);
        PasswordChangeRequestDto requestDto = PasswordChangeRequestDto.builder()
                .oldPassword("@Abc1234")
                .newPassword("@Change123").build();

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(true);

        //when
        memberService.changePassword(requestDto,"USER@spring");

        //then
        Member findMember = mockMemberRepo.findByEmail("USER@spring").get();
        Assertions.assertThat(encryptionService.matches("@Change123", findMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 정보 변경 - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 회원_정보_변경_실패_비밀번호_불일치() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);
        PasswordChangeRequestDto requestDto = PasswordChangeRequestDto.builder().build();

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(false);

        //when & then
        assertThrows(BadPasswordException.class,
                () -> memberService.changePassword(requestDto, "USER@spring")
        );

        //then
        Member findMember = mockMemberRepo.findByEmail("USER@spring").get();
        Assertions.assertThat(findMember.getPassword()).isEqualTo("@Abc1234");
    }

    @Test
    @DisplayName("회원 정보 변경 - 회원이 존재하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 회원정보변경_실패_회원없음() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);
        PasswordChangeRequestDto requestDto = PasswordChangeRequestDto.builder().build();

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(MemberNotFoundException.class,
                () -> memberService.changePassword(requestDto, "USER@spring")
        );
    }
}
