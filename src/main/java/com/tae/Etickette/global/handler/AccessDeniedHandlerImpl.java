package com.tae.Etickette.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 인증은 되어 있지만, 권한이 없는 사용자가 접근할 때 호출
 * 필터 체인에서 요청이 인증은 되었지만, URL 접근 권한이 부족할 때 발생
 */
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.NO_PERMISSION;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), "권한이 없습니다.");
        response.setStatus(errorCode.getStatus());
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
