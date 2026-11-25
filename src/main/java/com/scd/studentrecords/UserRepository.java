package com.scd.studentrecords;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDetails findByUsername(String username) {

        String sql = """
            SELECT username, password, role, enabled
            FROM users
            WHERE username = ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            User.withUsername(rs.getString("username"))
                .password(rs.getString("password"))
                .roles(rs.getString("role"))
                .disabled(!rs.getBoolean("enabled"))
                .build(),
            username
        ).stream()
         .findFirst()
         .orElseThrow(() ->
             new UsernameNotFoundException("User not found")
         );
    }

    public boolean existsByUsername(String username) {

        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = ?",
            Integer.class,
            username
        );

        return count != null && count > 0;
    }

    public void registerUser(String username, String encodedPassword) {

        String sql = """
            INSERT INTO users (username, password, role, enabled)
            VALUES (?, ?, 'USER', TRUE)
            """;

        jdbcTemplate.update(sql, username, encodedPassword);
    }
}
