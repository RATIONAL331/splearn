package com.example.splearn.application.provided;

import com.example.splearn.SplearnTestConfiguration;
import com.example.splearn.domain.*;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ContextConfiguration(classes = {SplearnTestConfiguration.class})
class MemberRegisterIntegrationTest {
	@Autowired
	private MemberRegister memberRegister;

	@Autowired
	private EntityManager entityManager;

	@Test
	void register() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));

		Assertions.assertThat(member.getId()).isNotNull();
		Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmail() {
		memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		Assertions.assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com")))
		          .isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	void memberRegisterRequestFail() {
		MemberRegisterRequest nickNameSizeFailedReq = new MemberRegisterRequest(
				"test@test.com",
				"password",
				"test"
		);
		Assertions.assertThatThrownBy(() -> {
			memberRegister.register(nickNameSizeFailedReq);
		}).isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	void activate() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		entityManager.flush();
		entityManager.clear();

		Member activate = memberRegister.activate(member.getId());
		entityManager.flush();

		Assertions.assertThat(activate.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}
}
