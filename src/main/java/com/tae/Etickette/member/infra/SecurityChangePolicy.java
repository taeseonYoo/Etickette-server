package com.tae.Etickette.member.infra;

import com.tae.Etickette.member.domain.ChangePolicy;
import com.tae.Etickette.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityChangePolicy implements ChangePolicy {
    @Override
    public boolean hasUpdatePermission(Member member, String requestEmail) {
        return isRequestMember(member, requestEmail) || isCurrentUserAdminRole();
    }

    private boolean isRequestMember(Member member, String requestEmail) {
        return member.getEmail().equals(requestEmail);
    }

    private boolean isCurrentUserAdminRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return false;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) return false;
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}
