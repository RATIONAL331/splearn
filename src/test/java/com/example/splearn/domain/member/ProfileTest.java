package com.example.splearn.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
	@Test
	void profile() {
		// when & then
		new Profile("asdf");
	}

	@Test
	void profileWithEmptyName() {
		// when & then
		assertThrows(IllegalArgumentException.class, () -> new Profile(""));
	}

	@Test
	void profileWithNonAlphabet() {
		// when & then
		assertThrows(IllegalArgumentException.class, () -> new Profile("!@#$"));
	}

	@Test
	void url() {
		// when & then
		Profile test = new Profile("test");
		Assertions.assertThat(test.url()).isEqualTo("@test");
	}

}