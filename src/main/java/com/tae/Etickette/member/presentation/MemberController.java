package com.tae.Etickette.member.presentation;

import com.tae.Etickette.member.application.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.application.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.application.dto.PasswordChangeRequestDto;
import com.tae.Etickette.member.application.dto.ProfileResponseDto;
import com.tae.Etickette.member.application.dto.MemberDeleteRequestDto;
import com.tae.Etickette.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<ProfileResponseDto> profile(Authentication authentication) {
        return new ResponseEntity<>(memberService.getProfile(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody MemberJoinRequestDto requestDto) {

        MemberJoinResponseDto responseDto = memberService.join(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@Valid @RequestBody PasswordChangeRequestDto requestDto,
                                       Authentication authentication) {

        memberService.changePassword(requestDto, authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> delete(@Valid @RequestBody MemberDeleteRequestDto memberDeleteRequestDto, Authentication authentication) {

        memberService.deleteMember(memberDeleteRequestDto, authentication.getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
