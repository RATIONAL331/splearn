package com.example.splearn.application.required;

import com.example.splearn.domain.Email;

public interface EmailSender {
	void send(Email email, String subject, String body);
}
