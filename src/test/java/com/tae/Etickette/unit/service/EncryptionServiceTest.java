package com.tae.Etickette.unit.service;


import com.tae.Etickette.global.auth.EncryptionService;
import com.tae.Etickette.global.auth.EncryptionServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit - EncryptionService")
public class EncryptionServiceTest {
    private EncryptionService encryptionService;

    @BeforeEach
    public void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        encryptionService = new EncryptionServiceImpl(passwordEncoder);
    }

    @Test
    @DisplayName("encode - 암호화에 성공한다.")
    public void 암호화_성공() {
        //given
        String rawPassword = "@RawPassword123";
        //when
        String encodedPassword = encryptionService.encode(rawPassword);
        //then
        Assertions.assertThat(encryptionService.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("encode - 평문이 null이면, 암호화에 실패한다.")
    public void 암호화_실패_평문누락() {
        //given
        String rawPassword = null;
        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> encryptionService.encode(rawPassword));
    }

    @Test
    @DisplayName("matches - 암호 비교에 성공한다.")
    public void 비교_성공() {
        //given
        String rawPassword = "@RawPassword123";
        String encodedPassword = encryptionService.encode(rawPassword);
        //when
        boolean matches = encryptionService.matches(rawPassword, encodedPassword);
        //then
        Assertions.assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("matches - 평문이 null이면 ,암호 비교에 실패한다.")
    public void 비교_실패_평문누락() {
        //given
        String rawPassword = "@RawPassword123";
        String encodedPassword = encryptionService.encode(rawPassword);
        //when & then
        assertThrows(IllegalArgumentException.class,
                () -> encryptionService.matches(null, encodedPassword));
    }

    @Test
    @DisplayName("matches - 암호가 null이면 ,암호 비교에 실패한다.")
    public void 비교_실패_암호누락() {
        //given
        String rawPassword = "@RawPassword123";
        String encodedPassword = encryptionService.encode(rawPassword);
        //when
        boolean matches = encryptionService.matches(rawPassword, null);
        //then
        Assertions.assertThat(matches).isFalse();
    }
}
