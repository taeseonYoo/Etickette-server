package com.tae.Etickette.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String secret = "myjwttestkeymyjwttestkeymyjwttes";
        jwtUtil = new JWTUtil(secret);
    }

    @Test
    @DisplayName("createJwt - ")
    void jwt_생성_성공() {
        //given
        String token = jwtUtil.createJwt("access", "test@spring", "USER", 1000L * 60 * 10);

        //then
        Assertions.assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("getEmail - ")
    void 이메일가져오기_성공() {
        //given
        String token = jwtUtil.createJwt("access", "test@spring", "USER", 1000L * 60 * 10);

        //then
        Assertions.assertThat(jwtUtil.getEmail(token)).isEqualTo("test@spring");
    }

    @Test
    @DisplayName("isExpired - 토큰이 만료되면, ExpiredJwtException이 발생한다.")
    void 토큰만료_오류_만료됨() throws InterruptedException {
        //given
        String token = jwtUtil
                .createJwt("access", "test@spring",
                        "USER", 1000L);
        //when
        Thread.sleep(1001);
        //then
        assertThrows(ExpiredJwtException.class, () ->
                jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("isExpired - 토큰이 만료되지 않으면, False를 리턴한다.")
    void 토큰만료_false_만료되지않음() throws InterruptedException {
        //given
        String token = jwtUtil
                .createJwt("access", "test@spring",
                        "USER", 1000L);

        //then
        assertFalse(jwtUtil.isExpired(token));
    }

    @Test
    @DisplayName("getRole - 역할 가져오기에 성공한다.")
    void 역할_성공() {
        //given
        String token = jwtUtil.createJwt("access", "test@spring", "USER", 1000L * 60 * 10);

        //when
        String role = jwtUtil.getRole(token);

        //then
        Assertions.assertThat(role).isEqualTo("USER");
    }

    @Test
    @DisplayName("getCategory - 카테고리 가져오기에 성공한다.")
    void 카테고리_성공() {
        //given
        String token = jwtUtil.createJwt("access", "test@spring", "USER", 1000L * 60 * 10);

        //when
        String category = jwtUtil.getCategory(token);

        //then
        Assertions.assertThat(category).isEqualTo("access");
    }

    @Test
    @DisplayName("getEmail - 이메일 가져오기에 성공한다.")
    void 이메일_성공() {
        //given
        String token = jwtUtil.createJwt("access", "test@spring", "USER", 1000L * 60 * 10);

        //when
        String email = jwtUtil.getEmail(token);

        //then
        Assertions.assertThat(email).isEqualTo("test@spring");
    }
}