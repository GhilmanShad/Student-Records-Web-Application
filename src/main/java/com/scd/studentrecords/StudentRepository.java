package com.scd.studentrecords;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {
    private final JdbcTemplate jdbc;

    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Student> studentRowMapper = (rs, rowNum) -> new Student(
            rs.getInt("id"),
            rs.getString("reg_no"),
            rs.getString("name"),
            rs.getString("department"),
            rs.getDouble("cgpa"));

    public List<Student> findAll() {
        return jdbc.query("SELECT * FROM students ORDER BY name ASC", studentRowMapper);
    }
    public List<Student> searchStudents(String keyword) {
        String sql = """
            SELECT * FROM students
            WHERE LOWER(name) LIKE LOWER(?) ESCAPE '!'
               OR LOWER(reg_no) LIKE LOWER(?) ESCAPE '!'
               OR LOWER(department) LIKE LOWER(?) ESCAPE '!'
            ORDER BY name ASC
            """;

        String escaped = keyword.replace("!", "!!")
                                .replace("%", "!%")
                                .replace("_", "!_");

        String pattern = "%" + escaped + "%";

        return jdbc.query(sql, studentRowMapper,
                          pattern, pattern, pattern);
    }

    public Student findByRegNo(String regNo) {
        String sql = "SELECT * FROM students WHERE reg_no = ?";
        List<Student> matches = jdbc.query(sql, studentRowMapper, regNo);

        if (matches.isEmpty()) {
            return null;
        }
        return matches.get(0);
    }
    public boolean existsByRegNo(String regNo) {
        String sql = "SELECT COUNT(*) FROM students WHERE reg_no = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, regNo);
        return count != null && count > 0;
    }
    public int insert(Student s) {
        String sql = "INSERT INTO students (reg_no, name, department, cgpa) VALUES (?, ?, ?, ?)";
        return jdbc.update(sql, s.getRegNo(), s.getName(), s.getDepartment(), s.getCgpa());
    }
    public int update(Student s) {
        return update(s.getRegNo(), s);
    }

    public int update(String originalRegNo, Student s) {
        String sql = "UPDATE students SET reg_no = ?, name = ?, department = ?, cgpa = ? WHERE reg_no = ?";
        return jdbc.update(sql, s.getRegNo(), s.getName(), s.getDepartment(), s.getCgpa(), originalRegNo);
    }
    public int deleteByRegNo(String regNo) {
        String sql = "DELETE FROM students WHERE reg_no = ?";
        return jdbc.update(sql, regNo);
    }




}
