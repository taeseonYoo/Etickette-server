package com.tae.Etickette.global.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2JwtHeaderController {
    private final OAuth2JwtHeaderService oauth2JwtHeaderService;

    @PostMapping("/oauth2-jwt-header")
    public void oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        oauth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
    }
}
