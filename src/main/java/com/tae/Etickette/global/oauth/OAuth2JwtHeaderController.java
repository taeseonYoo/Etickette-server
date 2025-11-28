package com.tae.Etickette.global.oauth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2 API", description = "OAuth2 관련 API")
@RestController
@RequiredArgsConstructor
public class OAuth2JwtHeaderController {
    private final OAuth2JwtHeaderService oauth2JwtHeaderService;

    @Operation(summary = "OAuth2 AccessToken 헤더 전달 ",
            description = "클라이언트의 access token 쿠키를 확인하고, Authorization 헤더에 토큰을 설정")
    @PostMapping("/oauth2-jwt-header")
    public void oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        oauth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }
}
