package com.tae.Etickette.integration.repository;

import com.tae.Etickette.member.entity.Member;
import com.tae.Etickette.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원 저장 - 회원 저장에 성공한다.")
    public void 회원_저장_성공() {
        //given
        Member member = Member.create("회원", "user@spring.com", "12345678");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 회원이 없습니다"));

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 저장 - 이메일이 중복 될 수 없다.")
    public void 회원_저장_이메일_중복() {
        //given
        Member member1 = Member.create("회원1", "user@spring.com", "12345678");
        Member member2 = Member.create("회원2", "user@spring.com", "12345678");

        //when ,then
        memberRepository.save(member1);
        assertThrows(Exception.class,
                () -> memberRepository.save(member2));
    }

    @Test
    @DisplayName("회원 수정 - 회원 수정에 성공한다.")
    public void 회원_수정_성공() {
        //given
        Member member = Member.create("회원", "user@spring.com", "12345678");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 회원이 없습니다."));
        findMember.updateEmail("change@spring.com");
        em.flush();

        //then
        Member findUpdateMember = memberRepository.findById(savedMember.getId())
                .orElseThrow(() -> new RuntimeException("저장 된 회원이 없습니다."));
        assertThat(findUpdateMember).isSameAs(member);
        assertThat(findUpdateMember.getEmail()).isEqualTo("change@spring.com");

    }

    @Test
    @DisplayName("회원 삭제 - 회원 삭제에 성공한다.")
    public void 회원_삭제_성공() {
        //given
        Member member = Member.create("회원", "user@spring.com", "12345678");
        memberRepository.save(member);
        Long memberId = member.getId();

        //when
        memberRepository.delete(member);

        //then
        Optional<Member> deletedMember = memberRepository.findById(memberId);
        assertThat(deletedMember).isEmpty();
    }
}