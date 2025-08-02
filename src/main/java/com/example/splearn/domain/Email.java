package com.example.splearn.domain;

import java.util.regex.Pattern;

//@Embeddable
public record Email(
//		@Column(name = "email_address", nullable = false)
		String address
) {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

	public Email {
		if (address.isBlank()) {
			throw new IllegalArgumentException("Email address cannot be null or blank");
		}

		if (!EMAIL_PATTERN.matcher(address).matches()) {
			throw new IllegalArgumentException("Invalid email format: " + address);
		}
	}
}
