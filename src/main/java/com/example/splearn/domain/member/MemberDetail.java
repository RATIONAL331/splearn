package com.example.splearn.domain.member;

import com.example.splearn.domain.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
	private Profile profile;

	private String introduction;

	private LocalDateTime registeredAt;

	private LocalDateTime activatedAt;

	private LocalDateTime deactivatedAt;

	static MemberDetail create() {
		MemberDetail memberDetail = new MemberDetail();
		memberDetail.registeredAt = LocalDateTime.now();
		return memberDetail;
	}

	void activate() {
		Assert.isTrue(activatedAt == null, "MemberDetail is already activated");
		this.activatedAt = LocalDateTime.now();
	}

	public void deactivate() {
		Assert.isTrue(deactivatedAt == null, "MemberDetail is already deactivated");
		this.deactivatedAt = LocalDateTime.now();
	}

	public void updateInfo(MemberInfoUpdateRequest request) {
		this.profile = new Profile(request.profileAddress());
		this.introduction = Objects.requireNonNull(request.introduction());
	}
}
