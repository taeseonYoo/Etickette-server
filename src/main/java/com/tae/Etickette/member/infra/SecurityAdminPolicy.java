package com.tae.Etickette.member.infra;

import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.global.exception.ForbiddenException;
import com.tae.Etickette.member.domain.AdminPolicy;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityAdminPolicy implements AdminPolicy {
    @Override
    public void checkIsAdmin() {
        if (!isAdmin()) {
            throw new ForbiddenException(ErrorCode.NO_PERMISSION, "회원 권한을 변경할 수 있는 권한이 없습니다.");
        }
    }

    private boolean isAdmin() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return false;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) return false;
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN"));
    }
}
