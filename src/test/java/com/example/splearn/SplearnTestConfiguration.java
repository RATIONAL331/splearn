package com.example.splearn;


import com.example.splearn.application.member.required.EmailSender;
import com.example.splearn.domain.member.MemberFixture;
import com.example.splearn.domain.member.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {
	@Bean
	public EmailSender emailSender() {
		return (email, subject, body) -> System.out.println("[TEST] Email sent to: [" + email + "] with subject:[ " + subject + "] and body: [" + body + "]");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return MemberFixture.createPasswordEncoder();
	}
}
