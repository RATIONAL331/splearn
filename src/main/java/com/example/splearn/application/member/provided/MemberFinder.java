package com.example.splearn.application.member.provided;

import com.example.splearn.domain.member.Member;

public interface MemberFinder {
	Member find(Long memberId);
}
