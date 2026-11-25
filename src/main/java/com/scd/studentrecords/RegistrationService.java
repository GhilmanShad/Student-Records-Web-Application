package com.scd.studentrecords;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DuplicateKeyException;
import java.nio.charset.StandardCharsets;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username,
                         String password,
                         String confirmPassword) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                "Username is required."
            );
        }

        username = username.strip();

        if (!username.matches("[a-zA-Z0-9_]{3,30}")) {
            throw new IllegalArgumentException(
                "Username must contain 3-30 letters, numbers or underscores."
            );
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                "Password must contain at least 8 characters."
            );
        }

        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                "Password is too long."
            );
        }

        if (!password.matches("(?s).*[A-Z].*")
                || !password.matches("(?s).*[a-z].*")
                || !password.matches("(?s).*[0-9].*")) {

            throw new IllegalArgumentException(
                "Password must contain uppercase, lowercase and numeric characters."
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                "Passwords do not match."
            );
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                "Username already exists."
            );
        }

        String encodedPassword = passwordEncoder.encode(password);

        try {
            userRepository.registerUser(username, encodedPassword);
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException(
                "Username already exists."
            );
        }
    }
}
