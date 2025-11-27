package com.tae.Etickette.member.application;

import com.tae.Etickette.member.domain.AdminPolicy;
import com.tae.Etickette.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminPolicy adminPolicy;
    private final MemberFinder memberFinder;

    @Transactional
    public void grantAdminRole(String email) {
        adminPolicy.checkIsAdmin();
        Member member = memberFinder.findMemberByEmailOrThrow(email);
        member.grantAdminRole();
    }

    @Transactional
    public void revokeAdminRole(String email) {
        adminPolicy.checkIsAdmin();
        Member member = memberFinder.findMemberByEmailOrThrow(email);
        member.revokeAdminRole();
    }
}
