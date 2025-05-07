package com.tae.Etickette.global.jwt;

import com.tae.Etickette.global.auth.CustomUserDetails;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.entity.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//요청에 대해 한 번만 동작하는 OncePerRequestFilter
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        token = request.getHeader("Authorization");

        // token null
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // access token expired
        try{
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        //토큰에서 email role 획득
        String email = jwtUtil.getEmail(token);
        Role role = Role.valueOf(jwtUtil.getRole(token));

        Authentication auth = null;


        Member member = Member.create("tempuser", email, "temppassword", role);
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());


        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);


    }
}
