package com.tae.Etickette;

import com.tae.Etickette.schedule.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByScheduleId(Long scheduleId);
}
