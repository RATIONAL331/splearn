package com.example.splearn.domain;

public record MemberCreateRequest(
		String email,
		String password,
		String nickname
) {
}
