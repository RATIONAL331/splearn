package com.example.splearn.application;

import com.example.splearn.application.provided.MemberFinder;
import com.example.splearn.application.provided.MemberRegister;
import com.example.splearn.application.required.EmailSender;
import com.example.splearn.application.required.MemberRepository;
import com.example.splearn.domain.*;
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

	private void checkDuplicateEmail(MemberRegisterRequest request) {
		memberRepository.findByEmail(new Email(request.email()))
		                .ifPresent(member -> {
			                           throw new DuplicateEmailException("이미 등록된 이메일입니다: " + request.email());
		                           }
		                );
	}

	private void sendWelcomeEmail(Member member) {
		emailSender.send(member.getEmail(), "Welcome to SP Learn", "Thank you for registering with SP Learn!");
	}
}
