package com.example.splearn.domain;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {
	@Test
	void createMember() {
		Member member = new Member("test@test.com", "password123", "testuser");
		Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}
}