package com.tae.Etickette.global.oauth;

import com.tae.Etickette.member.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Oauth2UserDto {
    private String name;
    private String email;
    private Role role;
}
