package com.tae.Etickette.member.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("ACTIVE"),
    DELETE("DELETE");
    final String status;

}
