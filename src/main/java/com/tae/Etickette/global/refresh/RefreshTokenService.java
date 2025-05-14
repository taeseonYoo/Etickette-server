package com.tae.Etickette.global.refresh;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RefreshTokenService {
    private final RefreshRepository refreshRepository;

    public RefreshTokenService(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    @Transactional
    public void saveRefresh(String email, String refresh, Integer expiredMs) {
        Refresh refreshEntity = Refresh.create(email, refresh, new Date(System.currentTimeMillis() + expiredMs * 1000L).toString());

        refreshRepository.save(refreshEntity);
    }
}
