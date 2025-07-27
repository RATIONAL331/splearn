package com.example.splearn.domain;

import lombok.Getter;

@Getter
public class Member {
	private String email;
	private String password;
	private String nickname;
	private MemberStatus status;

	public Member(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.status = MemberStatus.PENDING;
	}
}
