package com.tae.Etickette.member.domain;


public interface MemberChangePolicy {
    boolean hasUpdatePermission(Member member, String requestEmail);
}
