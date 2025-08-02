package com.example.splearn.domain;

public record MemberRegisterRequest(
		String email,
		String password,
		String nickname
) {
}
