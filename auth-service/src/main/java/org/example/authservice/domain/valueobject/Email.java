package org.example.authservice.domain.valueobject;

import java.util.regex.Pattern;

public class Email {
    // RFC 5322 Compliant Regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    private final String value;

    public Email(String rawValue) {
        this.value = cleanAndValidateEmail(rawValue);
    }

    private String cleanAndValidateEmail(String email) {
        // 1. Check null or empty
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or empty.");
        }

        // 2. Normalize: Trim spaces and convert to lowercase
        String cleanedEmail = email.strip().toLowerCase();

        // 3. Validate format using regex
        if (!EMAIL_PATTERN.matcher(cleanedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }

        // 4. Max length boundary check for database safety
        if (cleanedEmail.length() > 255) {
            throw new IllegalArgumentException("Email address must not exceed 255 characters.");
        }

        return cleanedEmail;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}