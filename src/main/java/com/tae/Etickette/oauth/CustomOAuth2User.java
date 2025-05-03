package com.tae.Etickette.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final UserDto userDto;

    public CustomOAuth2User(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        //획일화 하기 힘들어, 따로 구현한다.
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDto.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userDto.getName();
    }

    public String getEmail() {
        return userDto.getEmail();
    }
}
