package com.tae.Etickette.session.presentation;

import com.tae.Etickette.global.api.SuccessResponse;
import com.tae.Etickette.session.application.CancelSessionService;
import com.tae.Etickette.session.application.Dto.RegisterSessionRequest;
import com.tae.Etickette.session.application.Dto.RegisterSessionResponse;
import com.tae.Etickette.session.application.RegisterSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Session API", description = "스케줄 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sessions")
public class SessionController {
    private final RegisterSessionService registerSessionService;
    private final CancelSessionService cancelSessionService;

    @Operation(summary = "스케줄 등록", description = "공연Id와 공연 날짜, 공연 시간 리스트를 입력받아 스케줄을 생성한다.")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse<RegisterSessionResponse>> register(@RequestBody RegisterSessionRequest request) {
        registerSessionService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.success(new RegisterSessionResponse(request.getConcertId())));
    }

    @Operation(summary = "스케줄 취소", description = "스케줄Id를 입력받아 스케줄을 취소한다.")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{sessionId}/cancel")
    public ResponseEntity<SuccessResponse<Void>> cancel(@PathVariable Long sessionId) {
        cancelSessionService.cancel(sessionId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(null));
    }

}
