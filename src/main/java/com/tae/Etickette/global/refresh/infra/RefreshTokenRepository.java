package com.tae.Etickette.global.refresh.infra;

import com.tae.Etickette.global.refresh.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Boolean existsByRefresh(String refresh);
    void deleteByRefresh(String refresh);
    void deleteByEmail(String email);
}
