package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.command.domain.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatReleaseJob implements Job {
    private final BookSeatRepository bookSeatRepository;
    private final Scheduler scheduler;
    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long seatId = context.getMergedJobDataMap().getLong("seatId");
        Long sessionId = context.getMergedJobDataMap().getLong("sessionId");

        bookSeatRepository.findById(new BookSeatId(seatId,sessionId)).ifPresent(seat->{
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.cancel();
                log.info("delete success : status {},seatId {}",seat.getStatus(),seat.getSeatId());
            } else if (seat.getStatus() == SeatStatus.BOOKED) {
                try {
                    scheduler.deleteJob(
                            JobKey.jobKey("seat-" + seatId + "-" + sessionId, "seat-release")
                    );
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
