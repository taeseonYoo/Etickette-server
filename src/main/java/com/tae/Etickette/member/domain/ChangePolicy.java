package com.tae.Etickette.member.domain;


public interface ChangePolicy {
    boolean hasUpdatePermission(Member member, String requestEmail);
}
