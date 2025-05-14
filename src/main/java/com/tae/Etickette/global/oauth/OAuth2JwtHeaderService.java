package com.tae.Etickette.global.oauth;

import com.tae.Etickette.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class OAuth2JwtHeaderService {
    public void oauth2JwtHeaderSet(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String authorization = null;

        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("Authorization")){
                    authorization = cookie.getValue();
                }
            }
        }

        if(authorization == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 클라이언트의 authorization 토큰 쿠키를 만료한다.
        response.addCookie(CookieUtil.createCookie("Authorization", null, 0));
        response.addHeader("Authorization", "Bearer " + authorization);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
