package com.example.splearn.adapter.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SecurePasswordEncoderTest {
	@Test
	void securePasswordEncoder() {
		SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();
		String rawPassword = "password123";
		String encodedPassword = securePasswordEncoder.encode(rawPassword);

		Assertions.assertThat(securePasswordEncoder.matches(rawPassword, encodedPassword)).isTrue();
	}

}