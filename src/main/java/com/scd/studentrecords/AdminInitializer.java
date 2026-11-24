package com.scd.studentrecords;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder) {

        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = ?",
            Integer.class,
            "admin"
        );

        if (count != null && count == 0) {

            String password = System.getenv("ADMIN_PASSWORD");

            if (password == null || password.length() < 8
                    || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
                System.out.println(
                    "Admin account not created. Set a valid ADMIN_PASSWORD."
                );
                return;
            }

            jdbcTemplate.update(
                "INSERT INTO users (username, password, role, enabled) VALUES (?, ?, ?, ?)",
                "admin",
                passwordEncoder.encode(password),
                "ADMIN",
                true
            );

            System.out.println("Administrator account created successfully.");
        }
    }
}
