package com.tae.Etickette.member.domain;

public enum MemberStatus {
    ACTIVE("ACTIVE"),
    DELETE("DELETE");
    String status;
    MemberStatus(String status) {
        this.status = status;
    }

    public String value() {
        return status;
    }
}
