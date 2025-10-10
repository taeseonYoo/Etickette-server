package com.tae.Etickette.bookseat.infra;

import com.tae.Etickette.booking.command.domain.Booking;
import com.tae.Etickette.global.model.Canceller;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Unit - SecurityCancelSeatPolicy")
class SecurityCancelSeatPolicyTest {
    SecurityCancelSeatPolicy securityCancelSeatPolicy;
    private SecurityContext securityContext;
    private Authentication authentication;
    private Booking mockBooking;

    @BeforeEach
    void setUp() {
        securityCancelSeatPolicy = new SecurityCancelSeatPolicy();
        mockBooking = mock(Booking.class);
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);

        BDDMockito.given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    @DisplayName("예약자와 취소자가 같으면 true를 반환해야한다.")
    void hasCancellationPermission_booker() {
        //given
        BDDMockito.given(mockBooking.getMemberId()).willReturn(1L);
        BDDMockito.given(authentication.getAuthorities()).willReturn(Collections.emptyList());
        //when & then
        Assertions.assertThat(
                securityCancelSeatPolicy.hasCancellationPermission(mockBooking, new Canceller(1L)))
                .isTrue();
    }
    @Test
    @DisplayName("admin이 좌석을 취소하면 true를 반환해야한다.")
    void hasCancellationPermission_admin() {
        //given
        BDDMockito.given(mockBooking.getMemberId()).willReturn(2L);
        Collection<GrantedAuthority> authorityList = Arrays.asList(
                new SimpleGrantedAuthority("USER"),
                new SimpleGrantedAuthority("ADMIN")
        );
        doReturn(authorityList).when(authentication).getAuthorities();
        //when & then
        Assertions.assertThat(
                        securityCancelSeatPolicy.hasCancellationPermission(mockBooking, new Canceller(1L)))
                .isTrue();
    }
    @Test
    @DisplayName("예약자와 취소자가 다르면 false를 반환해야한다.")
    void hasCancellationPermission_fail() {
        //given
        Canceller canceller = new Canceller(1L);
        BDDMockito.given(mockBooking.getMemberId()).willReturn(2L);

        //when & then
        Assertions.assertThat(
                        securityCancelSeatPolicy.hasCancellationPermission(mockBooking, canceller))
                .isFalse();

    }
    @Test
    @DisplayName("SecurityContext가 비어있으면 false를 반환한다.")
    void hasCancellationPermission_fail_context() {
        //given
        SecurityContextHolder.clearContext();
        Canceller canceller = new Canceller(1L);
        BDDMockito.given(mockBooking.getMemberId()).willReturn(2L);

        //when & then
        Assertions.assertThat(
                        securityCancelSeatPolicy.hasCancellationPermission(mockBooking, canceller))
                .isFalse();
    }
    @Test
    @DisplayName("Authentication이 비어있으면 false를 반환한다.")
    void hasCancellationPermission_fail_authentication() {
        //given
        BDDMockito.given(securityContext.getAuthentication()).willReturn(null);
        Canceller canceller = new Canceller(1L);
        BDDMockito.given(mockBooking.getMemberId()).willReturn(2L);

        //when & then
        Assertions.assertThat(
                        securityCancelSeatPolicy.hasCancellationPermission(mockBooking, canceller))
                .isFalse();
    }

}