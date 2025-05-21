package com.tae.Etickette.concert.infra;

import com.tae.Etickette.concert.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section,Long> {
}
