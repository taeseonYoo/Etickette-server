package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.command.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long> {
}
