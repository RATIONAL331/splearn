package com.example.splearn.application.member.provided;

import com.example.splearn.SplearnTestConfiguration;
import com.example.splearn.domain.member.Member;
import com.example.splearn.domain.member.MemberFixture;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ContextConfiguration(classes = {SplearnTestConfiguration.class})
class MemberFinderTest {
	@Autowired
	private MemberFinder memberFinder;

	@Autowired
	private MemberRegister memberRegister;

	@Autowired
	private EntityManager entityManager;

	@Test
	void find() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		entityManager.flush();
		entityManager.clear();

		Member foundMember = memberFinder.find(member.getId());

		Assertions.assertThat(foundMember.getId()).isEqualTo(member.getId());
	}

	@Test
	void findFail() {
		Assertions.assertThatThrownBy(() -> memberFinder.find(999L))
		          .isInstanceOf(IllegalArgumentException.class);
	}
}