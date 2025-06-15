package com.tae.Etickette.member.application;

import com.tae.Etickette.member.domain.ChangePolicy;
import com.tae.Etickette.member.domain.EncryptionService;
import com.tae.Etickette.member.application.dto.*;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.infra.MemberRepository;
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
    private final ChangePolicy changePolicy;
    @Transactional
    public RegisterMemberResponse register(RegisterMemberRequest requestDto) {

        boolean duplicated = memberRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다.");
        }

        Member member = requestDto.toEntity(encryptionService, requestDto.getPassword());

        Member savedMember = memberRepository.save(member);
        return RegisterMemberResponse.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest requestDto, String requestEmail) {
        Member member = memberRepository.findByEmail(requestEmail).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        //TODO Changer 객체로 전달할까?
        if (!changePolicy.hasUpdatePermission(member, requestEmail)) {
            throw new NoChangeablePermission("회원 정보 수정 권한이 없습니다.");
        }

        member.changePassword(encryptionService, requestDto);
    }

    @Transactional
    public void deleteMember(DeleteMemberRequest requestDto, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));

        boolean match = member.matchPassword(encryptionService, requestDto.getPassword());
        if (!match) {
            throw new NoChangeablePermission("회원 정보 수정 권한이 없습니다.");
        }
        //TODO 토큰 삭제를 해야한다.

        member.deleteMember();
    }

    public ProfileResponse getProfile(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
        return ProfileResponse.builder().name(member.getName()).email(member.getEmail()).build();
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));
    }
}
