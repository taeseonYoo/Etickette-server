package com.tae.Etickette.member.infra;

import com.tae.Etickette.member.domain.ChangePolicy;
import com.tae.Etickette.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class SecurityChangePolicy implements ChangePolicy {
    @Override
    public boolean hasUpdatePermission(Member member, String requestEmail) {
        return isRequestMember(member, requestEmail);
    }

    private boolean isRequestMember(Member member, String requestEmail) {
        return member.getEmail().equals(requestEmail);
    }
}
