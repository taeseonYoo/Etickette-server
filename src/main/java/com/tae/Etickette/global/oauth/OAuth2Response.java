package com.tae.Etickette.global.oauth;

public interface OAuth2Response {
    //제공자 (Ex. google, github)
    String getProvider();
    //제공자에서 발급해주는 아이디
    String getProviderId();
    //이메일
    String getEmail();
    //사용자 실명 (설정한 이름)
    String getName();
}
