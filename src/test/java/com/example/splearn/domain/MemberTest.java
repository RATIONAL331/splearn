package com.example.splearn.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static com.example.splearn.domain.MemberFixture.createPasswordEncoder;

class MemberTest {
	private Member normalMember;
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		passwordEncoder = createPasswordEncoder();
		normalMember = Member.register(createMemberRegisterRequest("test@test.com"), passwordEncoder);
	}

	@Test
	void registerMember() {
		Assertions.assertThat(normalMember.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void constructorNullCheck() {
		Assertions.assertThatThrownBy(() -> Member.register(createMemberRegisterRequest(null), passwordEncoder))
		          .isInstanceOf(NullPointerException.class);
	}

	@Test
	void activateMember() {
		normalMember.activate();

		Assertions.assertThat(normalMember.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail() {
		normalMember.activate();

		Assertions.assertThatThrownBy(normalMember::activate)
		          .isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivateMember() {
		;
		normalMember.activate();

		normalMember.deactivate();

		Assertions.assertThat(normalMember.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	void deactivateFail() {
		Assertions.assertThatThrownBy(normalMember::deactivate)
		          .isInstanceOf(IllegalStateException.class);

		normalMember.activate();

		normalMember.deactivate();

		Assertions.assertThatThrownBy(normalMember::deactivate)
		          .isInstanceOf(IllegalStateException.class);
	}

	@Test
	void verifyPassword() {
		Assertions.assertThat(normalMember.verifyPassword("password123", passwordEncoder)).isTrue();
		Assertions.assertThat(normalMember.verifyPassword("wrongpassword", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname() {
		Assertions.assertThat(normalMember.getNickname()).isEqualTo("testuser");

		String newNickname = "newnickname";
		normalMember.changeNickname(newNickname);

		Assertions.assertThat(normalMember.getNickname()).isEqualTo(newNickname);
	}

	@Test
	void changePassword() {
		String newPassword = "newpassword123";
		normalMember.changePassword(newPassword, passwordEncoder);

		Assertions.assertThat(normalMember.verifyPassword(newPassword, passwordEncoder)).isTrue();
		Assertions.assertThat(normalMember.verifyPassword("password123", passwordEncoder)).isFalse();
	}

	@Test
	void isActive() {
		Assertions.assertThat(normalMember.isActive()).isFalse();

		normalMember.activate();

		Assertions.assertThat(normalMember.isActive()).isTrue();

		normalMember.deactivate();

		Assertions.assertThat(normalMember.isActive()).isFalse();
	}

	@Test
	void invalidEmail() {
		Assertions.assertThatThrownBy(() -> {
			// Attempt to create a member with an invalid email
			Member.register(createMemberRegisterRequest("invalid-email"), passwordEncoder);
		}).isInstanceOf(IllegalArgumentException.class);

	}
}