package com.example.splearn.application.member;

import com.example.splearn.application.member.provided.MemberFinder;
import com.example.splearn.application.member.required.MemberRepository;
import com.example.splearn.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
	private final MemberRepository memberRepository;

	@Override
	public Member find(Long memberId) {
		return memberRepository.findById(memberId)
		                       .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));
	}
}
