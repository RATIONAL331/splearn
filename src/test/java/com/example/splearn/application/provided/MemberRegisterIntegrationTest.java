package com.example.splearn.application.provided;

import com.example.splearn.SplearnTestConfiguration;
import com.example.splearn.domain.DuplicateEmailException;
import com.example.splearn.domain.Member;
import com.example.splearn.domain.MemberFixture;
import com.example.splearn.domain.MemberStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ContextConfiguration(classes = {SplearnTestConfiguration.class})
public class MemberRegisterIntegrationTest {
	@Autowired
	private MemberRegister memberRegister;

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
}
