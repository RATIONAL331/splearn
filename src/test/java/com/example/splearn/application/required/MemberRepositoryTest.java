package com.example.splearn.application.required;

import com.example.splearn.domain.Member;
import com.example.splearn.domain.MemberFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class MemberRepositoryTest {
	@Autowired
	private  MemberRepository memberRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void createMember() {
		Member member = Member.register(MemberFixture.createMemberRegisterRequest("test@test.com"), MemberFixture.createPasswordEncoder());

		Assertions.assertThat(member.getId()).isNull();

		memberRepository.save(member);

		Assertions.assertThat(member.getId()).isNotNull();

		entityManager.flush();
	}

	@Test
	void duplicateEmailFailed() {
		Member member1 = Member.register(MemberFixture.createMemberRegisterRequest("test@test.com"), MemberFixture.createPasswordEncoder());
		memberRepository.save(member1);

		Member member2 = Member.register(MemberFixture.createMemberRegisterRequest("test@test.com"), MemberFixture.createPasswordEncoder());
		Assertions.assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

}