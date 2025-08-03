package com.example.splearn.application.member.provided;

import com.example.splearn.domain.member.Member;
import com.example.splearn.domain.member.MemberInfoUpdateRequest;
import com.example.splearn.domain.member.MemberRegisterRequest;
import jakarta.validation.Valid;

/**
 * 회원 등록과 관련된 기능을 제공
 */
public interface MemberRegister {
	Member register(@Valid MemberRegisterRequest request);

	Member activate(Long memberId);

	Member deactivate(Long memberId);

	Member update(Long memberId, @Valid MemberInfoUpdateRequest request);
}
