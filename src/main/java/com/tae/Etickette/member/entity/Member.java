package com.tae.Etickette.member.entity;

import com.tae.Etickette.member.dto.PasswordUpdateRequestDto;
import com.tae.Etickette.member.service.BadPasswordException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public void updateName(String name) {
        this.name = name;
    }
    public void updateEmail(String email) {
        this.email=email;
    }
    public void updatePassword(String password) {
        this.password = password;
    }


    private Member(String name, String email, String password, Role role) {
        this.name=name;
        this.email = email;
        this.password=password;
        this.role = role;
    }
    public static Member create(String name, String email, String password, Role role) {
        return new Member(name, email, password, role);
    }

    public void changePassword(PasswordEncoder passwordEncoder, PasswordUpdateRequestDto requestDto) {
        if (!passwordEncoder.matches(requestDto.getOldPassword(), this.password)) {
            throw new BadPasswordException("비밀번호가 일치하지 않습니다.");
        }
        this.password = passwordEncoder.encode(requestDto.getNewPassword());
    }
}
