package com.example.splearn.adapter.integration;

import com.example.splearn.application.required.EmailSender;
import com.example.splearn.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {
	@Override
	public void send(Email email, String subject, String body) {
		System.out.println("[DUMMY] Email sent to: [" + email + "] with subject: [" + subject + "] and body: [" + body + "]");
	}
}
