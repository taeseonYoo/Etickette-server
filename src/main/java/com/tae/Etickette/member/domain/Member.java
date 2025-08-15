package com.tae.Etickette.member.domain;

import com.tae.Etickette.global.event.Events;
import com.tae.Etickette.global.exception.BadRequestException;
import com.tae.Etickette.global.exception.ErrorCode;
import com.tae.Etickette.member.application.dto.ChangePasswordRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Async;


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

    private void matchPassword(EncryptionService encryptionService, String password) {
        if(!encryptionService.matches(password, this.password)){
            throw new BadRequestException(ErrorCode.PASSWORD_NOT_MATCH,"비밀번호가 일치하지 않습니다.");
        }
    }

    public void changePassword(EncryptionService encryptionService, String oldPassword, String newPassword) {
        matchPassword(encryptionService, oldPassword);
        this.password = encryptionService.encode(newPassword);
    }

    /**
     * 회원 삭제 시, 회원의 refresh token 을 삭제한다.
     */
    public void deleteMember() {
        this.memberStatus = MemberStatus.DELETE;
        Events.raise(new MemberDeletedEvent(this.getEmail()));
    }
    //어드민 계정으로 변경
    public void grantAdminRole() {
        this.role = Role.ADMIN;
    }
}
