package com.tae.Etickette.global.refresh.infra;

import com.tae.Etickette.global.refresh.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
    void deleteByEmail(String email);
}
