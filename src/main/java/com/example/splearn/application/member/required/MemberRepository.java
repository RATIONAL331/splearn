package com.example.splearn.application.member.required;

import com.example.splearn.domain.member.Profile;
import com.example.splearn.domain.shared.Email;
import com.example.splearn.domain.member.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * 회원 정보를 저장하거나 조회
 */
public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);

	Optional<Member> findByEmail(Email email);

	Optional<Member> findById(Long id);

	@Query("SELECT m from Member m WHERE m.detail.profile = :profile")
	Optional<Member> findByProfile(Profile profile);
}
