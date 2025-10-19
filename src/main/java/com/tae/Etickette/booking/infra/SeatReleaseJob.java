package com.tae.Etickette.booking.infra;

import com.tae.Etickette.bookseat.command.domain.BookSeatId;
import com.tae.Etickette.bookseat.command.domain.SeatStatus;
import com.tae.Etickette.bookseat.infra.BookSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@DisallowConcurrentExecution
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
            } else if (seat.getStatus() == SeatStatus.BOOKED) {
                try {
                    scheduler.deleteJob(
                            JobKey.jobKey("seat-" + seatId + "-" + sessionId, "seat-release")
                    );
                    log.info("스케줄 삭제 성공 job => {}", context.getJobDetail().getKey());
                } catch (SchedulerException e) {
                    log.info("스케줄 삭제 실패 job => {},{}", context.getJobDetail().getKey(),e.getMessage());
                }
            }
        });
    }
}
