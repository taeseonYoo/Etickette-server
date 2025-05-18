package com.tae.Etickette.member.infra;

import com.tae.Etickette.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>{
    Optional<Member> findByEmail(String email);
}
