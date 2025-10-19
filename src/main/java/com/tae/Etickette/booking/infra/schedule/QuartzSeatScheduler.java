package com.tae.Etickette.booking.infra.schedule;

import com.tae.Etickette.booking.command.domain.SeatScheduler;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzSeatScheduler implements SeatScheduler {
    private final Scheduler scheduler;
    @Override
    @Transactional
    public void scheduling(List<Long> seatIds, Long sessionId) {
        for (Long seatId : seatIds) {
            JobDetail jobDetail = JobBuilder.newJob(SeatReleaseJob.class)
                    .withIdentity("seat-" + seatId + "-" + sessionId, "seat-release")
                    .usingJobData("seatId", seatId)
                    .usingJobData("sessionId", sessionId)
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .startAt(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                    .build();
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                log.error("Scheduler Exception {}", e.getMessage());
                //TODO 관리자 알림 기능 또는 재시도 로직
            }
        }
    }
}
