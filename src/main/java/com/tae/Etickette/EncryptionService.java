package com.tae.Etickette;

public interface EncryptionService {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
