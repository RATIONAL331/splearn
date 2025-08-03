package com.example.splearn.domain.member;

public class MemberFixture {
	public static MemberRegisterRequest createMemberRegisterRequest(String mail) {
		return new MemberRegisterRequest(mail, "password123", "testuser");
	}

	public static PasswordEncoder createPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(String rawPassword) {
				return rawPassword; // For testing, we return the raw password directly
			}

			@Override
			public boolean matches(String rawPassword, String encodedPassword) {
				return encode(rawPassword).equals(encodedPassword);
			}
		};
	}
}
