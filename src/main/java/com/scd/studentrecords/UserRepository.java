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
}
