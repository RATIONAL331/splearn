package com.example.splearn.domain;

/**
 * 해당 인터페이스는 반드시 domain 패키지에 위치해야 합니다.
 * Application(required) 계층으로 이동하면 이를 이용하는 엔티티(Member)의 의존관계가 뒤집힙니다.
 */
public interface PasswordEncoder {
	String encode(String rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}
