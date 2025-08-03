package com.example.splearn.domain.member;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.splearn.domain.member.MemberFixture.createMemberRegisterRequest;
import static com.example.splearn.domain.member.MemberFixture.createPasswordEncoder;

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
		Assertions.assertThat(normalMember.getDetail().getRegisteredAt()).isNotNull();
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
		Assertions.assertThat(normalMember.getDetail().getActivatedAt()).isNotNull();
	}

	@Test
	void activateFail() {
		normalMember.activate();

		Assertions.assertThatThrownBy(normalMember::activate)
		          .isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivateMember() {
		normalMember.activate();

		normalMember.deactivate();

		Assertions.assertThat(normalMember.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
		Assertions.assertThat(normalMember.getDetail().getDeactivatedAt()).isNotNull();
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

	@Test
	void updateInfo() {
		normalMember.activate();

		MemberInfoUpdateRequest request = new MemberInfoUpdateRequest(
				"newnickname",
				"profileAddress",
				"Hello, I am a test user."
		);
		normalMember.updateInfo(request);

		Assertions.assertThat(normalMember.getNickname()).isEqualTo(request.nickname());
		Assertions.assertThat(normalMember.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
		Assertions.assertThat(normalMember.getDetail().getIntroduction()).isEqualTo(request.introduction());
	}

	@Test
	void updateInfoFailed() {
		MemberInfoUpdateRequest request = new MemberInfoUpdateRequest(
				"newnickname",
				"thisisprofileAddress",
				"Hello, I am a test user."
		);

		Assertions.assertThatThrownBy(() ->
				normalMember.updateInfo(request)
		).isInstanceOf(IllegalStateException.class);
	}
}