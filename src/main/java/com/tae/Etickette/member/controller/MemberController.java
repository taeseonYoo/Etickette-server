package com.tae.Etickette.member.controller;

import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> join(@Valid @RequestBody MemberJoinRequestDto request) {
        /**
         * TODO GLOBAL EXCEPTION HANDLER
         */
        MemberJoinResponseDto responseDto = memberService.join(request);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
