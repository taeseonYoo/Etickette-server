package com.tae.Etickette.global.refresh.application;

import com.tae.Etickette.global.jwt.JWTUtil;
import com.tae.Etickette.global.util.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public ReissueService(JWTUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    //TODO 응용계층의 HttpServletRequest와 HttpServeltResponse에 의존적이라, 테스트가 가능한지 모르겠다.
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        // 쿠키에 refresh토큰이 없다면, 400 오류
        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //토큰이 만료되었다면, 400 오류
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // refresh DB조회
        Boolean isExist = refreshTokenService.existsByRefresh(refresh);

        // DB에 없거나, 블랙리스트 처리된 리프레시 토큰
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", email, role, 1000L * 60 * 10);
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 1000L * 60 * 60 * 24);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenService.deleteByRefresh(refresh);
        refreshTokenService.saveRefresh(email, newRefresh, 60 * 60 * 24);

        //response
        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh, 60 * 60 * 24));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
