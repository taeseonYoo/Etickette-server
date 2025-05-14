package com.tae.Etickette.refresh.infra;

import com.tae.Etickette.refresh.domain.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefresh(String refresh);
    @Transactional
    void deleteByRefresh(String refresh);
}
