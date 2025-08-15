package com.tae.Etickette.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출
 * 필터 체인에서 인증 정보가 없는 경우, JWT 토큰이 없는 경우
 */
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), "인증되지 않은 사용자입니다.");
        response.setStatus(errorCode.getStatus());
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
