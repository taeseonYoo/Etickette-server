package com.tae.Etickette.member.domain;

public interface EncryptionService {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
