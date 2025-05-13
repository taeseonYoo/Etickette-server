package com.tae.Etickette.global.oauth;

import com.tae.Etickette.RefreshTokenService;
import com.tae.Etickette.global.util.CookieUtil;
import com.tae.Etickette.global.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * OAuth2 로그인 성공 후 JWT 발급
 *
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public CustomSuccessHandler(JWTUtil jwtUtil,RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", email, role, 1000L * 60 * 10);
        String refresh = jwtUtil.createJwt("refresh", email, role, 1000L * 60 * 60 * 24);

        refreshTokenService.saveRefresh(email, refresh, 60 * 60 * 24);

        response.addCookie(CookieUtil.createCookie("Authorization", access, 60 * 10));
        response.addCookie(CookieUtil.createCookie("refresh", refresh, 60 * 60 * 24));

        response.sendRedirect("http://localhost:3000/oauth2-jwt-header");
    }

}
