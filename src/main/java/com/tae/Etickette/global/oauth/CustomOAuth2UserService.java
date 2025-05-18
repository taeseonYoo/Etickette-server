package com.tae.Etickette.global.oauth;

import com.tae.Etickette.member.domain.Member;
import com.tae.Etickette.member.domain.Role;
import com.tae.Etickette.member.infra.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // registrationId를 확인한다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        // registrationId에 맞게 전처리 과정을 거친다.
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("github")) {
            //TODO 깃허브 OAuth2 설정을 완료해야한다.
        } else {
            return null;
        }

        //이메일을 사용하여 검증한다.
        String email = oAuth2Response.getEmail();
        //OAuth 로그인은 비밀번호가 필요 없으므로 임의의 값을 설정한다.
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        Optional<Member> findMember = memberRepository.findByEmail(email);

        //TODO username 방식에서 email로 검증하는 방식으로 수정하면서 수정이 필요해 보인다.
        if (findMember.isEmpty()){
            Member member = Member.create(oAuth2Response.getName(), email, password, Role.USER);
            memberRepository.save(member);

            OAuth2UserDto oauth2UserDto = new OAuth2UserDto();
            oauth2UserDto.setEmail(email);
            oauth2UserDto.setName(oAuth2Response.getName());
            oauth2UserDto.setRole(Role.USER);

            return new CustomOAuth2User(oauth2UserDto);
        }else {
            Member member = findMember.get();
            member.updateName(oAuth2Response.getName());

            OAuth2UserDto oauth2UserDto = new OAuth2UserDto();
            oauth2UserDto.setEmail(email);
            oauth2UserDto.setName(oAuth2Response.getName());
            oauth2UserDto.setRole(member.getRole());

            return new CustomOAuth2User(oauth2UserDto);
        }


    }
}
