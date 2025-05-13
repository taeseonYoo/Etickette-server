package com.tae.Etickette.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Claims getPayload(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
    //암호화 된 데이터를 verifyWith로 검증
    public String getEmail(String token) {
        return getPayload(token).get("email", String.class);
    }

    public String getRole(String token) {
        return getPayload(token).get("role", String.class);
    }

    public String getCategory(String token) {
        return getPayload(token).get("category", String.class);
    }
    //현재 시간 값과 비교하여, 만료되었는 지 확인한다.
    public Boolean isExpired(String token) {
        return getPayload(token).getExpiration().before(new Date());
    }
    //토큰 생성
    public String createJwt(String category,String email, String role,Long expiredMs) {
        return Jwts.builder()
                .claim("category",category)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
