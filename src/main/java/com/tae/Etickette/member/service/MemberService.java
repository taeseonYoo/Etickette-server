package com.tae.Etickette.member.service;

import com.tae.Etickette.member.dto.MemberJoinResponseDto;
import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.dto.MemberJoinRequestDto;
import com.tae.Etickette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberJoinResponseDto join(MemberJoinRequestDto memberJoinRequestDto) {

        boolean duplicated = memberRepository.findByEmail(memberJoinRequestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        //비밀번호를 암호화 한다.
        String encodedPassword = passwordEncoder.encode(memberJoinRequestDto.getPassword());
        Member member = memberJoinRequestDto.toEntity(encodedPassword);

        Member savedMember = memberRepository.save(member);
        return MemberJoinResponseDto.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("사용자가 없음"));
    }


}
