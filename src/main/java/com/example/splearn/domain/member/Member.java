package com.example.splearn.domain.member;

import com.example.splearn.domain.AbstractEntity;
import com.example.splearn.domain.shared.Email;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache // NaturalId 가 붙은 것으로 조회할 시 캐시 사용
//@Table(uniqueConstraints = @UniqueConstraint(name = "UK_EMAIL_ADDRESS", columnNames = "email_address"))
public class Member extends AbstractEntity {
//	@Embedded
	@NaturalId // NaturalId 는 고유한 값으로, 중복이 허용되지 않음
	private Email email;

//	@Column(nullable = false)
	private String password;

//	@Column(nullable = false)
	private String nickname;

//	@Enumerated(EnumType.STRING)
//	@Column(length = 50, nullable = false)
	private MemberStatus status;

	private MemberDetail detail;

	public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder) {
		Member member = new Member();

		member.email = new Email(request.email());
		member.password = passwordEncoder.encode(Objects.requireNonNull(request.password()));
		member.nickname = Objects.requireNonNull(request.nickname());
		member.status = MemberStatus.PENDING;

		member.detail = MemberDetail.create();

		return member;
	}

	public void activate() {
//		if (getStatus() != MemberStatus.PENDING) {
//			throw new IllegalStateException("Member is not in PENDING status");
//		}
		Assert.state(MemberStatus.PENDING.equals(this.status), "Member is not in PENDING status");

		this.status = MemberStatus.ACTIVE;
		this.detail.activate();
	}

	public void deactivate() {
		Assert.state(MemberStatus.ACTIVE.equals(this.status), "Member is not in ACTIVE status");

		this.status = MemberStatus.DEACTIVATED;
		this.detail.deactivate();
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

	public void updateInfo(MemberInfoUpdateRequest request) {
		Assert.state(MemberStatus.ACTIVE.equals(this.status), "Member is not in ACTIVE status");

		this.nickname = Objects.requireNonNull(request.nickname());
		this.detail.updateInfo(request);
	}
}
