package com.tae.Etickette.global.oauth;

import com.tae.Etickette.global.jwt.JWTUtil;
import com.tae.Etickette.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class OAuth2JwtHeaderService {
    public void oauth2JwtHeaderSet(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String access = null;

        if(cookies != null){
            access = CookieUtil.getCookieValue(cookies, JWTUtil.AUTH_HEADER);
        }

        if(access == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 클라이언트의 access 토큰 쿠키를 만료한다.
        response.addCookie(CookieUtil.createCookie(JWTUtil.AUTH_HEADER, null, 0));
        response.addHeader(JWTUtil.AUTH_HEADER, JWTUtil.BEARER_PREFIX + access);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
