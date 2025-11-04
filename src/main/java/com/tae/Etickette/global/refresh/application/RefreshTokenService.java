package com.tae.Etickette.global.refresh.application;

import com.tae.Etickette.global.refresh.domain.RefreshToken;
import com.tae.Etickette.global.refresh.infra.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefresh(String email, String refresh, Long expiredMs) {
        RefreshToken refreshToken = new RefreshToken(email, refresh,
                expiredMs);

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteByRefresh(String refresh) {
        refreshTokenRepository.deleteByRefresh(refresh);
    }

    @Transactional
    public void deleteByMember(String memberEmail) {
        refreshTokenRepository.deleteById(memberEmail);
    }

    public Boolean existsByRefresh(String refresh){
        return refreshTokenRepository.existsByRefresh(refresh);
    }

}
