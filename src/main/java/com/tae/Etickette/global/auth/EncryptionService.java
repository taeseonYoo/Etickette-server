package com.tae.Etickette.global.auth;

public interface EncryptionService {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
