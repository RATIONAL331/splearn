package com.example.splearn.domain;

public interface PasswordEncoder {
	String encode(String rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}
