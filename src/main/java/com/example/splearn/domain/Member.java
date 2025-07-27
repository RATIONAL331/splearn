package com.example.splearn.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
public class Member {
	private String email;
	private String password;
	private String nickname;
	private MemberStatus status;

	public Member(String email, String password, String nickname) {
		this.email = Objects.requireNonNull(email);;
		this.password = Objects.requireNonNull(password);
		this.nickname = Objects.requireNonNull(nickname);
		this.status = MemberStatus.PENDING;
	}

	public void activate() {
//		if (getStatus() != MemberStatus.PENDING) {
//			throw new IllegalStateException("Member is not in PENDING status");
//		}
		Assert.state(MemberStatus.PENDING.equals(this.status), "Member is not in PENDING status");

		this.status = MemberStatus.ACTIVE;
	}

	public void deactivate() {
		Assert.state(MemberStatus.ACTIVE.equals(this.status), "Member is not in PENDING status");

		this.status = MemberStatus.DEACTIVATED;
	}
}
