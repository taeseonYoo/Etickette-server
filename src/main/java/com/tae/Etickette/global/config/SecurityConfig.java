package com.tae.Etickette.global.config;

import com.tae.Etickette.global.refresh.application.RefreshTokenService;
import com.tae.Etickette.global.auth.CustomLogoutFilter;
import com.tae.Etickette.global.jwt.JWTFilter;
import com.tae.Etickette.global.jwt.JWTUtil;
import com.tae.Etickette.global.jwt.LoginFilter;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.global.oauth.CustomOAuth2UserService;
import com.tae.Etickette.global.oauth.CustomSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Security 인증 관련 설정을 담당한다.
 * @EnableWebSecurity : 이 클래스가 SpringSecurity에게 관리가 된다.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외 발생 시 처리
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {

        //cors
        http
                .cors((cors)->cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);
                                configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

                                return configuration;
                            }
                        }));
        // 로그아웃 요청 URL 설정
        http
                .logout((auth) -> auth.logoutSuccessUrl("/").logoutUrl("/api/members/logout").permitAll());
        //jwt를 통한 STATELESS 방식을 사용하므로, csrf 설정은 비활성화 한다.
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //oAuth2 설정
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)))
                        .successHandler(customSuccessHandler));

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
//                        .anyRequest().permitAll()
                        .requestMatchers("/", "/api/members/login", "/api/members/logout", "/api/members/signup",
                                "/oauth2-jwt-header", "/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/concerts","/api/concerts/**").permitAll()
                        .anyRequest().authenticated()
                );

        // - 인증되지 않은 사용자는 authenticationEntryPoint가 처리 (401 Unauthorized)
        // - 권한이 부족한 사용자는 accessDeniedHandler가 처리 (403 Forbidden)
        http.exceptionHandling(exceptions -> {
            exceptions.accessDeniedHandler(accessDeniedHandler);
            exceptions.authenticationEntryPoint(authenticationEntryPoint);
        });

        //custom logout filter 등록
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenService), LogoutFilter.class);

        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //커스텀 로그인 필터 등록 - 로그인 url 은 "/api/members/login"
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService);
        loginFilter.setFilterProcessesUrl("/api/members/login");
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
