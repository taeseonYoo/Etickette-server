package com.tae.Etickette.member.entity;

import com.tae.Etickette.global.auth.EncryptionService;
import com.tae.Etickette.member.dto.PasswordChangeRequestDto;
import com.tae.Etickette.member.service.BadPasswordException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;

    public void updateName(String name) {
        this.name = name;
    }
    public void updateEmail(String email) {
        this.email=email;
    }


    private Member(String name, String email, String password, Role role) {
        this.name=name;
        this.email = email;
        this.password=password;
        this.role = role;
        this.memberStatus = MemberStatus.ACTIVE;
    }
    public static Member create(String name, String email, String password, Role role) {
        return new Member(name, email, password, role);
    }

    public boolean matchPassword(EncryptionService encryptionService, String password) {
        return encryptionService.matches(password, this.password);
    }
    public void changePassword(EncryptionService encryptionService, PasswordChangeRequestDto requestDto) {
        if (!matchPassword(encryptionService, requestDto.getOldPassword())) {
            throw new BadPasswordException("비밀번호가 일치하지 않습니다.");
        }
        this.password = encryptionService.encode(requestDto.getNewPassword());
    }
    public void deleteMember() {
        this.memberStatus = MemberStatus.DELETE;
    }
}
