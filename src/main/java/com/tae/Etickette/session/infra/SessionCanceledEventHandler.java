package com.tae.Etickette.session.infra;

import com.tae.Etickette.booking.application.CancelBookingService;
import com.tae.Etickette.session.domain.SessionCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SessionCanceledEventHandler {
    private CancelBookingService cancelBookingService;

    public SessionCanceledEventHandler(CancelBookingService cancelBookingService) {
        this.cancelBookingService = cancelBookingService;
    }

//    @Async
    @EventListener(SessionCanceledEvent.class)
    public void handle(SessionCanceledEvent event) {
        cancelBookingService.cancelAll(event.getSessionId());
    }

}
