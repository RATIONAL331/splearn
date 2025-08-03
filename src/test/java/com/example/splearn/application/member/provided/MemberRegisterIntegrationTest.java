package com.example.splearn.application.member.provided;

import com.example.splearn.SplearnTestConfiguration;
import com.example.splearn.domain.member.*;
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
		Assertions.assertThat(activate.getDetail().getActivatedAt()).isNotNull();
	}

	@Test
	void deactivate() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		entityManager.flush();
		entityManager.clear();

		Member activate = memberRegister.deactivate(member.getId());
		entityManager.flush();

		Assertions.assertThat(activate.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
		Assertions.assertThat(activate.getDetail().getDeactivatedAt()).isNotNull();
	}

	@Test
	void update() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		member.activate();
		entityManager.flush();
		entityManager.clear();

		MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("newNickname", "newProfile", "new Introduction");
		Member updatedMember = memberRegister.update(member.getId(), updateRequest);
		entityManager.flush();
		entityManager.clear();

		Assertions.assertThat(updatedMember.getId()).isEqualTo(member.getId());
		Assertions.assertThat(updatedMember.getNickname()).isEqualTo(updateRequest.nickname());
		Assertions.assertThat(updatedMember.getDetail().getProfile().url()).isEqualTo("@" + updateRequest.profileAddress());
		Assertions.assertThat(updatedMember.getDetail().getIntroduction()).isEqualTo(updateRequest.introduction());

	}

	@Test
	void updateInfoDuplicateFailed() {
		String profileAddress = "profileAddress";

		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest("test@test.com"));
		member.activate();

		Member anotherMember = memberRegister.register(MemberFixture.createMemberRegisterRequest("test2@test.com"));
		anotherMember.activate();

		entityManager.flush();
		entityManager.clear();

		MemberInfoUpdateRequest request = new MemberInfoUpdateRequest(
				"newnickname",
				profileAddress,
				"Hello, I am a test user."
		);

		memberRegister.update(member.getId(), request);
		entityManager.flush();
		entityManager.clear();

		Assertions.assertThatThrownBy(() -> memberRegister.update(anotherMember.getId(), request))
		          .isInstanceOf(IllegalArgumentException.class);
	}
}
