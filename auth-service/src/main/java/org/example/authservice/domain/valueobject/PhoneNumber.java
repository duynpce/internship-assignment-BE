package org.example.authservice.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public class PhoneNumber {
    private final static Pattern PHONE_NUMBER_REGEX = Pattern.compile("^0\\d{9,13}$");


    private final String value;

    public PhoneNumber(String value) {
        this.value = cleanAndValidatePhoneNumber(value);
    }

    public String getValue() {
        return value;
    }

    private String cleanAndValidatePhoneNumber(String phoneNumber) {
        // 1. Check null or empty
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("phoneNumber address cannot be null or empty.");
        }

        // 2. Normalize: Trim spaces and convert to lowercase
        String cleanedPhoneNumber = phoneNumber.strip().toLowerCase();

        // 3. Validate format using regex
        if (!PHONE_NUMBER_REGEX.matcher(cleanedPhoneNumber).matches()) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }

        // 4. Max length boundary check for database safety
        if (cleanedPhoneNumber.length() > 255) {
            throw new IllegalArgumentException("phone number address must not exceed 255 characters.");
        }

        return cleanedPhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
