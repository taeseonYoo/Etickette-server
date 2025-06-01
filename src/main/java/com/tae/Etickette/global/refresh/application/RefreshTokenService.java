package com.tae.Etickette.global.refresh.application;

import com.tae.Etickette.global.refresh.domain.RefreshToken;
import com.tae.Etickette.global.refresh.infra.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void saveRefresh(String email, String refresh, Integer expiredMs) {
        RefreshToken refreshToken = RefreshToken.create(email, refresh,
                new Date(System.currentTimeMillis() + expiredMs * 1000L).toString());

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteByRefresh(String refresh) {
        refreshTokenRepository.deleteByRefresh(refresh);
    }

    public Boolean existsByRefresh(String refresh){
        return refreshTokenRepository.existsByRefresh(refresh);
    }

}
