package com.tae.Etickette.member.application;

import com.tae.Etickette.member.domain.EncryptionService;
import com.tae.Etickette.member.infra.EncryptionServiceImpl;
import com.tae.Etickette.member.BadPasswordException;
import com.tae.Etickette.member.MemberNotFoundException;
import com.tae.Etickette.member.application.dto.MemberDeleteRequestDto;
import com.tae.Etickette.member.application.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.application.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.application.dto.PasswordChangeRequestDto;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.MemberStatus;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
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
    @DisplayName("join - 회원 가입에 성공한다.")
    public void 회원가입() {
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
    @DisplayName("join - 중복된 이메일이 있으면, 회원 가입에 실패한다.")
    public void 회원가입_실패_중복이메일() {
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
        assertThrows(DuplicateKeyException.class,
                () -> memberService.join(requestDto));
    }
    @Test
    @DisplayName("join - 비밀번호가 누락되면, 회원 가입에 실패한다.")
    public void 회원가입_실패_비밀번호누락() {
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
    @DisplayName("join - 이메일이 누락되면, 회원 가입에 실패한다.")
    public void 회원가입_실패_이메일누락() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("사용자")
                .password("@Abc1234")
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
    @DisplayName("join - 이름이 누락되면, 회원 가입에 실패한다.")
    public void 회원가입_실패_이름누락() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .email("USER@spring")
                .password("@Abc1234")
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
    @DisplayName("changePassword - 비밀번호 변경에 성공한다.")
    public void 비밀번호변경_성공() {
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
    @DisplayName("changePassword - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 비밀번호변경_실패_비밀번호불일치() {
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
    @DisplayName("changePassword - 회원이 존재하지 않으면 ,비밀번호 변경에 실패한다.")
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

    @Test
    @DisplayName("deleteMember - 회원 삭제에 성공한다.")
    public void 회원삭제_성공() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(true);

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
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(false);

        MemberDeleteRequestDto requestDto = MemberDeleteRequestDto.builder()
                .password("@Fake1234").build();
        //when & then
        assertThrows(RuntimeException.class,
                () -> memberService.deleteMember(requestDto, "USER@spring"));
    }
    @Test
    @DisplayName("deleteMember - 회원 삭제에 실패하면, 회원의 Status는 ACTIVE")
    public void 회원삭제_실패_상태변경X() {
        //given
        Member member = Member.create("USER", "USER@spring", "@Abc1234", Role.USER);

        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(false);

        MemberDeleteRequestDto requestDto = MemberDeleteRequestDto.builder()
                .password("@Abc1234").build();
        //when & then
        assertThrows(RuntimeException.class,
                () -> memberService.deleteMember(requestDto, "USER@spring"));

        //then
        Member findMember = memberService.findByEmail("USER@spring");
        Assertions.assertThat(findMember.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);

    }
}
