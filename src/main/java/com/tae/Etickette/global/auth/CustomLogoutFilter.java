package com.tae.Etickette.global.auth;

import com.tae.Etickette.global.jwt.JWTUtil;
import com.tae.Etickette.global.refresh.application.RefreshTokenService;
import com.tae.Etickette.global.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response,chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //POST경로 logout요청을 확인한다.
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api\\/members\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            filterChain.doFilter(request, response);
            return;
        }

        //get refresh token
        String refresh = CookieUtil
                .getCookieValue(request.getCookies(), "refresh");

        //refresh null check
        if (refresh == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //TODO 예외 핸들러 학습 후, 예외를 던지는 방식으로 변경
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            //TODO 예외 핸들러 학습 후, 예외를 던지는 방식으로 변경
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenService.existsByRefresh(refresh);
        if (!isExist) {

            //TODO 예외 핸들러 학습 후, 예외를 던지는 방식으로 변경
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃을 진행한다.

        // 1. Refresh 토큰을 DB에서 제거
        refreshTokenService.deleteByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = CookieUtil.createCookie("refresh", null, 0);

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
