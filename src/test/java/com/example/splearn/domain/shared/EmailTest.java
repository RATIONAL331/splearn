package com.example.splearn.domain.shared;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailTest {
	@Test
	void equality() {
		Email email1 = new Email("asdf@test.com");
		Email email2 = new Email("asdf@test.com");

		Assertions.assertThat(email1).isEqualTo(email2);
	}

	@Test
	void notValid() {
		Assertions.assertThatThrownBy(() -> new Email("invalid-email"))
		          .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void nonNull() {
		Assertions.assertThatThrownBy(() -> new Email(null))
		          .isInstanceOf(NullPointerException.class);
	}
}