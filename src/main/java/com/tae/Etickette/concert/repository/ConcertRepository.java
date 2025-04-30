package com.tae.Etickette.concert.repository;

import com.tae.Etickette.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert,Long> {
}
