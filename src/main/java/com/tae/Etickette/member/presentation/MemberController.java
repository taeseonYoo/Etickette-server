package com.tae.Etickette.member.presentation;

import com.tae.Etickette.member.application.dto.DeleteMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberRequest;
import com.tae.Etickette.member.application.dto.RegisterMemberResponse;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import com.tae.Etickette.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/signup")
    public ResponseEntity<?> join(@Valid @RequestBody RegisterMemberRequest requestDto) {

        RegisterMemberResponse responseDto = memberService.register(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> update(@Valid @RequestBody ChangePasswordRequest requestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.changePassword(requestDto, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteMemberRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.deleteMember(request, email);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
