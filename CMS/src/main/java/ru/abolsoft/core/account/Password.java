package ru.abolsoft.core.account;

import java.security.SecureRandom;

public class Password {
    private final String value;

    public Password(String value) {
        if (isValid(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Invalid password format");
        }
    }

    public static Password generate() {
        String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return new Password(sb.toString());
    }
    private boolean isValid(String value) {
        return value != null && value.length() >= 8;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Password password = (Password) obj;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
