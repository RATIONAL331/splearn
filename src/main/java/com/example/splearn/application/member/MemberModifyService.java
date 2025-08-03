package com.example.splearn.application.member;

import com.example.splearn.application.member.provided.MemberFinder;
import com.example.splearn.application.member.provided.MemberRegister;
import com.example.splearn.application.member.required.EmailSender;
import com.example.splearn.application.member.required.MemberRepository;
import com.example.splearn.domain.member.*;
import com.example.splearn.domain.shared.Email;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
	private final MemberFinder memberFinder;
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(@Valid MemberRegisterRequest request) {
		// check
		checkDuplicateEmail(request);
		// domain model
		Member member = Member.register(request, passwordEncoder);
		// repository
		memberRepository.save(member);
		// post process
		sendWelcomeEmail(member);

		return member;
	}

	@Override
	public Member activate(Long memberId) {
		// check
		Member member = memberFinder.find(memberId);

		// domain model
		member.activate();

		return memberRepository.save(member); // 1. NoSQL을 쓰더라도 원본 코드 유지 가능 2. save 시 Event Publication 이용 가능
	}

	@Override
	public Member deactivate(Long memberId) {
		// check
		Member member = memberFinder.find(memberId);
		member.activate();

		// domain model
		member.deactivate();

		return memberRepository.save(member);
	}

	@Override
	public Member update(Long memberId, MemberInfoUpdateRequest request) {
		// check
		Member member = memberFinder.find(memberId);

		checkDuplicateProfile(member, request.profileAddress());

		// domain model
		member.updateInfo(request);

		return memberRepository.save(member);
	}

	private void checkDuplicateEmail(MemberRegisterRequest request) {
		memberRepository.findByEmail(new Email(request.email()))
		                .ifPresent(member -> {
			                           throw new DuplicateEmailException("이미 등록된 이메일입니다: " + request.email());
		                           }
		                );
	}

	private void checkDuplicateProfile(Member member, String profileAddress) {
		if (profileAddress == null || profileAddress.isBlank()) {
			return; // 프로필 주소가 비어있으면 중복 검사 생략
		}

		Profile profile = member.getDetail().getProfile();
		if (profile != null && profileAddress.equals(profile.address())) {
			return; // 같은 프로필 주소면 중복 아님
		}

		memberRepository.findByProfile(new Profile(profileAddress))
		                .ifPresent(existingMember -> {
			                           throw new IllegalArgumentException("이미 등록된 프로필 주소입니다: " + profileAddress);
		                           }
		                );
	}

	private void sendWelcomeEmail(Member member) {
		emailSender.send(member.getEmail(), "Welcome to SP Learn", "Thank you for registering with SP Learn!");
	}
}
