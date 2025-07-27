package com.example.splearn.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {
	@Test
	void createMember() {
		Member member = new Member("test@test.com", "password123", "testuser");
		Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void constructorNullCheck() {
		Assertions.assertThatThrownBy(() -> new Member(null, "password123", "testuser"))
		          .isInstanceOf(NullPointerException.class);
	}

	@Test
	void activateMember() {
		Member member = new Member("test@test.com", "password123", "testuser");

		member.activate();

		Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
	}

	@Test
	void activateFail() {
		Member member = new Member("test@test.com", "password123", "testuser");

		member.activate();

		Assertions.assertThatThrownBy(member::activate)
		          .isInstanceOf(IllegalStateException.class);
	}

	@Test
	void deactivateMember() {
		Member member = new Member("test@test.com", "password123", "testuser");
		member.activate();

		member.deactivate();

		Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
	void deactivateFail() {
		Member member = new Member("test@test.com", "password123", "testuser");

		Assertions.assertThatThrownBy(member::deactivate)
		          .isInstanceOf(IllegalStateException.class);

		member.activate();

		member.deactivate();

		Assertions.assertThatThrownBy(member::deactivate)
		          .isInstanceOf(IllegalStateException.class);
	}
}