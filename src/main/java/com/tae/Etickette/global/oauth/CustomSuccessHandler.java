package com.tae.Etickette.global.oauth;

import com.tae.Etickette.global.refresh.application.RefreshTokenService;
import com.tae.Etickette.global.util.CookieUtil;
import com.tae.Etickette.global.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.base-url}")
    private String baseUrl;
    @Value("${app.oauth2.spa-redirect-path}")
    private String redirectPath;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createAccessToken(email, role);
        String refresh = jwtUtil.createRefreshToken(email, role);

        refreshTokenService.saveRefresh(email, refresh, jwtUtil.getRefreshTokenExpiredMs());

        response.addCookie(CookieUtil.createCookie(JWTUtil.AUTH_HEADER, access, jwtUtil.getAccessTokenExpireSeconds()));
        response.addCookie(CookieUtil.createCookie(JWTUtil.REFRESH, refresh, jwtUtil.getRefreshTokenExpireSeconds()));

        response.sendRedirect(baseUrl + redirectPath);
    }

}
