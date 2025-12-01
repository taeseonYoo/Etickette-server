package com.tae.Etickette.global.refresh.presentation;

import com.tae.Etickette.global.api.SuccessResponse;
import com.tae.Etickette.global.refresh.application.ReissueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reissue API", description = "AccessToken 재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tokens")
public class ReissueController {
    private final ReissueService reissueService;

    @Operation(summary = "AccessToken 재발급", description = "RefreshToken으로 AccessToken을 재발급한다.")
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<Void>> reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueService.reissue(request, response);
        return ResponseEntity.status(HttpStatus.OK)
                .body(SuccessResponse.success(null));
    }
}
