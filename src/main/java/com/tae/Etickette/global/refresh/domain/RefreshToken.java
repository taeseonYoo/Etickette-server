package com.tae.Etickette.global.refresh.domain;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refreshToken")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    private String email;
    @Indexed
    private String refresh;
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

    public RefreshToken(String email, String refresh, long expiration) {
        this.email = email;
        this.refresh = refresh;
        this.expiration = expiration;
    }

}
