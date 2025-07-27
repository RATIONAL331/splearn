package com.example.splearn.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
	private Email email;
	private String password;
	private String nickname;
	private MemberStatus status;

	public static Member create(MemberCreateRequest request, PasswordEncoder passwordEncoder) {
		Member member = new Member();

		member.email = new Email(request.email());
		member.password = passwordEncoder.encode(Objects.requireNonNull(request.password()));
		member.nickname = Objects.requireNonNull(request.nickname());
		member.status = MemberStatus.PENDING;

		return member;
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

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, this.password);
	}

	public void changeNickname(String newNickname) {
		this.nickname = Objects.requireNonNull(newNickname);
	}

	public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(Objects.requireNonNull(newPassword));
	}

	public boolean isActive() {
		return MemberStatus.ACTIVE.equals(this.status);
	}
}
