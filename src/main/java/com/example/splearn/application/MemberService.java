package com.example.splearn.application;

import com.example.splearn.application.provided.MemberRegister;
import com.example.splearn.application.required.EmailSender;
import com.example.splearn.application.required.MemberRepository;
import com.example.splearn.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
	private final MemberRepository memberRepository;
	private final EmailSender emailSender;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Member register(MemberRegisterRequest request) {
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
