package com.tae.Etickette.member.application;

import com.tae.Etickette.member.domain.EncryptionService;
import com.tae.Etickette.member.application.dto.*;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
import com.tae.Etickette.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EncryptionService encryptionService;

    @Transactional
    public MemberJoinResponseDto join(MemberJoinRequestDto requestDto) {

        boolean duplicated = memberRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다.");
        }

        Member member = requestDto.toEntity(encryptionService, requestDto.getPassword());

        Member savedMember = memberRepository.save(member);
        return MemberJoinResponseDto.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    @Transactional
    public void changePassword(PasswordChangeRequestDto requestDto, String email) {
        //TODO OAUTH는 비밀번호를 수정할 수 없다.
        //TODO 수정에 성공하면 JWT를 재발급 해야한다?

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        member.changePassword(encryptionService, requestDto);
    }

    @Transactional
    public void deleteMember(MemberDeleteRequestDto requestDto, String email) {

        Member member = findByEmail(email);
        //TODO 비밀번호로 회원 검증
        boolean match = member.matchPassword(encryptionService, requestDto.getPassword());
        if (!match) {
            //TODO Exception 변경 예정
            throw new RuntimeException("");
        }
        //TODO 토큰 삭제 -> Refresh 도입 고민

        member.deleteMember();
    }

    public ProfileResponseDto getProfile(String email) {
        Member member = findByEmail(email);
        return ProfileResponseDto.builder().name(member.getName()).email(member.getEmail()).build();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
    }


}
