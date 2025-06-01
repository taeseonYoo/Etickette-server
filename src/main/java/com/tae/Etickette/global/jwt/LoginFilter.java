package com.tae.Etickette.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.global.refresh.application.RefreshTokenService;
import com.tae.Etickette.global.auth.CustomUserDetails;
import com.tae.Etickette.global.util.CookieUtil;
import com.tae.Etickette.member.application.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

/**
 * 일반 로그인
 * access : 10분
 * refresh : 24시간
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil,RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginRequestDto = objectMapper.readValue(messageBody, LoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공 시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", email, role, 1000L * 60 * 10);
        String refresh = jwtUtil.createJwt("refresh", email, role, 1000L * 60 * 60 * 24);

        refreshTokenService.saveRefresh(email, refresh, 60 * 60 * 24);
        //HTTP 인증 방식은 RFC 7235 정의에 따라 아래 인증 헤더 형태를 가져야 한다.
        response.addHeader("Authorization","Bearer " + access);
        response.addCookie(CookieUtil.createCookie("refresh",refresh,60 * 60 * 24));
        response.setStatus(HttpStatus.OK.value());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}
