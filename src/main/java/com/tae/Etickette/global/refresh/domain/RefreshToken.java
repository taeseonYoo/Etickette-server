package com.tae.Etickette.global.refresh.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String refresh;
    private String expiration;

    public RefreshToken(String email, String refresh, String expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }

    public static RefreshToken create(String email, String refresh, String expiration) {
        return new RefreshToken(email, refresh, expiration);
    }
}
