package com.tae.Etickette.member.presentation;

import com.tae.Etickette.global.api.SuccessResponse;
import com.tae.Etickette.global.api.ErrorResponse;
import com.tae.Etickette.member.application.MemberService;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member API", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "이름, 이메일, 패스워드를 입력하여 회원 가입을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "중복된 이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<RegisterMemberResponse>> join(
            @Valid @RequestBody RegisterMemberRequest requestDto) {

        RegisterMemberResponse responseDto = memberService.register(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success(responseDto));
    }

    @Operation(summary = "비밀번호 변경", description = "기존 비밀번호, 새로운 비밀번호를 입력하여 비밀번호를 변경한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 / 기존 비밀번호 불일치"),
            @ApiResponse(responseCode = "403", description = "회원 정보 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패")
    })
    @PutMapping("/profile")
    public ResponseEntity<Void> update(@Valid @RequestBody ChangePasswordRequest requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.changePassword(requestDto, email);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 삭제", description = "삭제할 회원의 이메일을 입력하여, 회원을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "403", description = "회원 정보 삭제 권한 없음")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteMemberRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.deleteMember(request, email);

        return ResponseEntity.noContent().build();
    }
}
