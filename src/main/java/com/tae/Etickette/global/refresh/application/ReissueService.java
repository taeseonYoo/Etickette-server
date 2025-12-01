package com.tae.Etickette.global.refresh.application;

import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.UnauthorizedException;
import com.tae.Etickette.global.jwt.JWTUtil;
import com.tae.Etickette.global.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        refresh = CookieUtil.getCookieValue(request.getCookies(), JWTUtil.REFRESH);

        // 쿠키에 refresh 토큰이 없다면, 401 오류
        if (refresh == null) {
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_IS_NULL, "refresh token is null");
        }

        //토큰이 만료되었다면, 401 오류
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_IS_EXPIRED, "refresh token expired");
        }

        // refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(JWTUtil.REFRESH)) {
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_IS_INVALID, "invalid refresh token");
        }

        // refresh DB조회
        Boolean isExist = refreshTokenService.existsByRefresh(refresh);

        // DB에 없거나, 블랙리스트 처리된 리프레시 토큰
        if (!isExist) {
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_IS_NOT_FOUND, "not found valid refresh token");
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createAccessToken(email, role);
        String newRefresh = jwtUtil.createRefreshToken(email, role);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenService.deleteByMember(email);
        refreshTokenService.saveRefresh(email, newRefresh, jwtUtil.getRefreshTokenExpiredMs());

        //response
        response.setHeader(JWTUtil.AUTH_HEADER, JWTUtil.BEARER_PREFIX + newAccess);
        response.addCookie(CookieUtil.createCookie(JWTUtil.REFRESH, newRefresh,
                jwtUtil.getRefreshTokenExpireSeconds()));
    }
}
