package com.example.splearn.application.provided;

import com.example.splearn.domain.Member;
import com.example.splearn.domain.MemberRegisterRequest;
import jakarta.validation.Valid;

/**
 * 회원 등록과 관련된 기능을 제공
 */
public interface MemberRegister {
	Member register(@Valid MemberRegisterRequest request);

	Member activate(Long memberId);
}
