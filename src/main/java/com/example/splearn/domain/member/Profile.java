package com.example.splearn.domain.member;

import java.util.regex.Pattern;

public record Profile(
		String address
) {
	private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9]+$");

	public Profile {
		if (address.isBlank()) {
			throw new IllegalArgumentException("Profile address cannot be null or blank");
		}

		if (address.length() > 150) {
			throw new IllegalArgumentException("Profile address cannot exceed 150 characters");
		}

		if (!PROFILE_ADDRESS_PATTERN.matcher(address).matches()) {
			throw new IllegalArgumentException("Invalid profile address format: " + address);
		}
	}

	public String url() {
		return "@" + address;
	}
}
