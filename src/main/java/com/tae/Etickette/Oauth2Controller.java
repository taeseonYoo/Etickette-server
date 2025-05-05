package com.tae.Etickette;

import com.tae.Etickette.concert.Oauth2JwtHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {
    private final Oauth2JwtHeaderService oauth2JwtHeaderService;

    @PostMapping("/oauth2-jwt-header")
    public String oauth2JwtHeader(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("oauth2");
        String s = oauth2JwtHeaderService.oauth2JwtHeaderSet(request, response);
        return s;
    }
}
