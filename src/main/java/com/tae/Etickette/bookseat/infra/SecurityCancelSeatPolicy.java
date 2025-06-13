package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.booking.domain.Booking;
import com.tae.Etickette.bookseat.domain.CancelSeatPolicy;
import com.tae.Etickette.global.model.Canceller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityCancelSeatPolicy implements CancelSeatPolicy {
    @Override
    public boolean hasCancellationPermission(Booking booking, Canceller canceller) {
        return isCancellerPermission(booking,canceller) || isCurrentUserAdminRole();
    }

    private boolean isCancellerPermission(Booking booking, Canceller canceller) {
        return booking.getMemberId().equals(canceller.getMemberId());
    }

    private boolean isCurrentUserAdminRole() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return false;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null) return false;
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
