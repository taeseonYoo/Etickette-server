package com.tae.Etickette.member.presentation;

import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.application.dto.ProfileResponse;
import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 정보 조회
     *
     * @param memberId
     * @param authentication
     * @return
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<ProfileResponse> profile(@PathVariable Long memberId, Authentication authentication) {
        return new ResponseEntity<>(memberService.getProfile(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestBody RegisterMemberRequest requestDto) {

        RegisterMemberResponse responseDto = memberService.register(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> update(@Valid @RequestBody ChangePasswordRequest requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.changePassword(requestDto, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteMemberRequest deleteMemberRequest, Authentication authentication) {

        memberService.deleteMember(deleteMemberRequest, authentication.getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
