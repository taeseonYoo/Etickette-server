package com.tae.Etickette.member.infra;

import com.tae.Etickette.global.refresh.application.RefreshTokenService;
import com.tae.Etickette.member.domain.MemberDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDeletedEventHandler {
    private final RefreshTokenService refreshTokenService;

    @EventListener(MemberDeletedEvent.class)
    public void handle(MemberDeletedEvent event) {
        refreshTokenService.deleteByMember(event.getEmail());
    }
}
