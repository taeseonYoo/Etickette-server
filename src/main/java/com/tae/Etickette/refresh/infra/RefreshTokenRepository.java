package com.tae.Etickette.refresh.infra;

import com.tae.Etickette.refresh.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
}
