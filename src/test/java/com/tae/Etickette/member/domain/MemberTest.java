package com.tae.Etickette.member.domain;

import com.tae.Etickette.global.event.Events;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Unit - Member")
class MemberTest {
    EncryptionService encryptionService = mock(EncryptionService.class);
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    @BeforeEach
    void setUp() {
        Events.setPublisher(publisher);
    }

    @Test
    @DisplayName("회원을 생성하면, status = ACTIVE 이다.")
    void 회원생성_성공() {
        //given & when
        Member member = Member.create("test", "abc@spring", "@Abc1234", Role.USER);

        //then
        Assertions.assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
    @Test
    @DisplayName("회원을 삭제하면, status = DELETE")
    void 회원삭제_성공() {
        //given
        Member member = Member.create("test", "abc@spring", "@Abc1234", Role.USER);

        //when
        member.deleteMember();

        //then
        Assertions.assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.DELETE);
    }
    @Test
    @DisplayName("회원을 삭제하면, MemberDeletedEvent 를 발행해야한다.")
    void 회원삭제_성공_이벤트() {
        //given
        Member member = Member.create("test", "abc@spring", "@Abc1234", Role.USER);

        //when
        member.deleteMember();

        //then
        verify(publisher).publishEvent(any(MemberDeletedEvent.class));
    }
    @Test
    @DisplayName("비밀번호 변경에 성공하면, 인코딩된 비밀번호가 저장된다.")
    void 비밀번호변경_성공() {
        //given
        Member member = Member.create("test", "abc@spring", "@OldPW123", Role.USER);
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(true);
        BDDMockito.given(encryptionService.encode("@NewPW123")).willReturn("ENCODEDNEWPW");
        //when
        member.changePassword(encryptionService
                , "@OldPW123",
                "@NewPW123");

        //then
        Assertions.assertThat(member.getPassword()).isEqualTo("ENCODEDNEWPW");
    }
    @Test
    @DisplayName("DB에 저장된 비밀번호와 oldPassword 가 일치하지 않으면, 변경에 실패한다.")
    void 비밀번호변경_실패_일치하지않음() {
        //given
        Member member = Member.create("test", "abc@spring", "@OldPW123", Role.USER);
        BDDMockito.given(encryptionService.matches(any(), any())).willReturn(false);

        //when & then
        assertThrows(BadPasswordException.class,
                () -> member.changePassword(encryptionService
                        , "@OldPW123",
                        "@NewPW123"));
    }

}