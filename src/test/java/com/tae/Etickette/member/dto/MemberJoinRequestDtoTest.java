package com.tae.Etickette.member.dto;

import com.tae.Etickette.member.application.dto.MemberJoinRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberJoinRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("password - 회원 가입 시 비밀번호에 빈문자열이면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호공백(){
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    @DisplayName("password - 회원 가입 시 비밀번호에 특수문자를 포함하지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호특수문자() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("Abc12345").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    @DisplayName("password - 회원 가입 시 비밀번호의 길이가 8보다 작으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호최소길이() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc123").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    @DisplayName("password - 회원 가입 시 비밀번호의 길이가 20보다 크면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호최대길이() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@Abc123@Abc123@Abc123@Abc123@Abc123@Abc123").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("password - 회원 가입 시 비밀번호에 대문자를 포함하지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호대문자() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@abc12345").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    @DisplayName("password - 회원 가입 시 비밀번호에 소문자를 포함하지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호소문자(){
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@ABC12345").build();

        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        assertFalse(violations.isEmpty());
    }
    @Test
    @DisplayName("password - 회원 가입 시 비밀번호에 숫자를 포함하지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_비밀번호숫자() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@spring")
                .password("@ABcdefgh").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);

        //then
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("email - 회원 가입 시 이메일 형식이 맞지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_이메일() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("test@")
                .password("@ABcd12345").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        boolean emailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        //then
        assertFalse(violations.isEmpty());
        assertTrue(emailViolation);
    }

    @Test
    @DisplayName("email - 회원 가입 시 이메일 형식이 맞지 않으면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_이메일2() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("@a")
                .password("@ABcd12345").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        boolean emailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        //then
        assertFalse(violations.isEmpty());
        assertTrue(emailViolation);
    }
    @Test
    @DisplayName("email - 회원 가입 시 이메일이 빈문자열이면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_이메일공백() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("test")
                .email("")
                .password("@ABcd12345").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        boolean emailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        //then
        assertFalse(violations.isEmpty());
        assertTrue(emailViolation);
    }
    @Test
    @DisplayName("name - 회원 가입 시 이름이 빈문자열이면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_이름공백() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("")
                .email("test@spring")
                .password("@ABcd12345").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        boolean emailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        //then
        assertFalse(violations.isEmpty());
        assertTrue(emailViolation);
    }
    @Test
    @DisplayName("name - 회원 가입 시 이름의 길이가 20보다 크면 ,비밀번호 유효성 검사에 실패한다.")
    void 유효성_실패_이름최대길이() {
        //given
        MemberJoinRequestDto requestDto = MemberJoinRequestDto.builder()
                .name("abcdefghijklmnopqrstu")
                .email("test@spring")
                .password("@ABcd12345").build();

        //when
        Set<ConstraintViolation<MemberJoinRequestDto>> violations = validator.validate(requestDto);
        boolean emailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));

        //then
        assertFalse(violations.isEmpty());
        assertTrue(emailViolation);
    }
}