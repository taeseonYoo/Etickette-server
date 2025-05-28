package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.domain.Concert;
import com.tae.Etickette.concert.domain.ConcertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long> {
}
