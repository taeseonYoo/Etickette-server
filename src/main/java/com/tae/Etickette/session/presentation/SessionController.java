package com.tae.Etickette.session;

import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.RegisterSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
public class SessionController {
    private final RegisterSessionService registerSessionService;

    /**
     * 공연 스케줄 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterSessionRequest request) {
        registerSessionService.register(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
