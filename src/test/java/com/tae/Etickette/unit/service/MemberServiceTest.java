package com.tae.Etickette.unit.service;

import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.dto.PasswordUpdateRequestDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.entity.Role;
import com.tae.Etickette.member.repository.MemberRepository;
import com.tae.Etickette.member.service.BadPasswordException;
import com.tae.Etickette.member.service.DuplicateEmailException;
import com.tae.Etickette.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("Unit - MemberService")
public class MemberServiceTest {

    private MemberService memberService;
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(mockMemberRepo, passwordEncoder);
    }

    @Test
    @DisplayName("회원 가입 - 회원 가입에 성공한다.")
    public void 회원_가입() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("tester")
                .email("test@spring.io")
                .password("12345678").build();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encodedPassword);
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
                .name("tester")
                .email("test@spring.io")
                .password("12345678").build();
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encodedPassword);
        ReflectionTestUtils.setField(member, "id", 1L);

        BDDMockito.given(mockMemberRepo.findByEmail(any()))
                .willReturn(Optional.of(member));

        //when
        assertThrows(DuplicateEmailException.class,
                () -> memberService.join(requestDto));
    }

    @Test
    @DisplayName("회원 정보 변경 - 비밀번호 변경에 성공한다.")
    public void 회원_정보_변경_성공() {
        //given
        Member member = Member.create("tester", "abc@spring.io", "@aBc1234", Role.USER);
        PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder()
                .oldPassword("@aBc1234")
                .newPassword("@Change123").build();
        Authentication authentication = BDDMockito.mock(Authentication.class);
        BDDMockito.given(authentication.getName()).willReturn("abc@spring.io");
        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(passwordEncoder.matches(any(), any())).willReturn(true);

        //when
        memberService.updatePassword(requestDto,"abc@spring.io");

        //then
        Member findMember = mockMemberRepo.findByEmail("abc@spring.io").get();
        Assertions.assertThat(findMember.getPassword()).isEqualTo("@Change123");
    }

    @Test
    @DisplayName("회원 정보 변경 - 기존 비밀번호가 일치하지 않으면 ,비밀번호 변경에 실패한다.")
    public void 회원_정보_변경_실패() {
        //given
        Member member = Member.create("tester", "abc@spring.io", "@aBc1234", Role.USER);
        PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder().build();
        Authentication authentication = BDDMockito.mock(Authentication.class);
        BDDMockito.given(authentication.getName()).willReturn("abc@spring.io");
        BDDMockito.given(mockMemberRepo.findByEmail(any())).willReturn(Optional.of(member));
        BDDMockito.given(passwordEncoder.matches(any(), any())).willReturn(false);

        //when & then
        assertThrows(BadPasswordException.class,
                () -> memberService.updatePassword(requestDto, "abc@spring.io")
        );

        //then
        Member findMember = mockMemberRepo.findByEmail("abc@spring.io").get();
        Assertions.assertThat(findMember.getPassword()).isEqualTo("@aBc1234");
    }
}
