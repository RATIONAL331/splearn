package com.example.splearn;


import com.example.splearn.application.required.EmailSender;
import com.example.splearn.domain.MemberFixture;
import com.example.splearn.domain.PasswordEncoder;
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
