package com.tae.Etickette.member.application;

import com.tae.Etickette.member.domain.ChangePolicy;
import com.tae.Etickette.member.domain.EncryptionService;
import com.tae.Etickette.member.application.dto.*;
import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
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

    /**
     * 회원 가입
     * @param requestDto
     * @return id, name, email
     */
    @Transactional
    public RegisterMemberResponse register(RegisterMemberRequest requestDto) {

        boolean duplicated = memberRepository.findByEmail(requestDto.getEmail()).isPresent();
        if (duplicated) {
            throw new AlreadyExisingEmailException("이미 사용 중인 이메일입니다.");
        }

        Member member = Member.create(requestDto.getName(), requestDto.getEmail(),
                encryptionService.encode(requestDto.getPassword()), Role.USER);


        Member savedMember = memberRepository.save(member);
        return RegisterMemberResponse.builder()
                .id(savedMember.getId())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .build();
    }

    /**
     * 비밀번호 변경
     * @param requestDto
     */
    @Transactional
    public void changePassword(ChangePasswordRequest requestDto,String requestEmail) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

        //TODO Changer 객체로 전달할까?
        if (!changePolicy.hasUpdatePermission(member, requestEmail)) {
            throw new NoChangeablePermission("회원 정보 수정 권한이 없습니다.");
        }

        member.changePassword(encryptionService, requestDto.getOldPassword(),requestDto.getNewPassword());
    }

    /**
     * 회원 삭제
     * @param deleteMemberRequest
     * @param requestEmail
     */
    @Transactional
    public void deleteMember(DeleteMemberRequest deleteMemberRequest, String requestEmail) {

        Member member = memberRepository.findByEmail(deleteMemberRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));

        if (!changePolicy.hasUpdatePermission(member, requestEmail)) {
            throw new NoChangeablePermission("회원 정보 삭제 권한이 없습니다.");
        }

        member.deleteMember();
    }
}
