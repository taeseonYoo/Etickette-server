package com.tae.Etickette.session.presentation;

import com.tae.Etickette.session.application.CancelSessionService;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    private final RegisterSessionService registerSessionService;
    private final CancelSessionService cancelSessionService;

    /**
     * 공연 스케줄 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterSessionRequest request) {
        registerSessionService.register(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{sessionId}/cancel")
    public ResponseEntity<Void> cancelSession(@PathVariable Long sessionId) {
        cancelSessionService.cancel(sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
