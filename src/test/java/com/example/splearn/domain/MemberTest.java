package com.example.splearn.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {
	private Member normalMember;
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		passwordEncoder = new PasswordEncoder() {
			@Override
			public String encode(String rawPassword) {
				return rawPassword; // For testing, we return the raw password directly
			}

			@Override
			public boolean matches(String rawPassword, String encodedPassword) {
				return encode(rawPassword).equals(encodedPassword);
			}
		};
		normalMember = Member.create(new MemberCreateRequest("test@test.com", "password123", "testuser"), passwordEncoder);
	}

	@Test
	void createMember() {
		Assertions.assertThat(normalMember.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void constructorNullCheck() {
		Assertions.assertThatThrownBy(() -> Member.create(new MemberCreateRequest(null, "password123", "testuser"), passwordEncoder))
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
			Member.create(new MemberCreateRequest("invalid-email", "password123", "testuser"), passwordEncoder);
		}).isInstanceOf(IllegalArgumentException.class);

	}
}