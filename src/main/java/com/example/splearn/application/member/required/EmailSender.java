package com.example.splearn.application.member.required;

import com.example.splearn.domain.shared.Email;

public interface EmailSender {
	void send(Email email, String subject, String body);
}
