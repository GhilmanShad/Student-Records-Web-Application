package com.scd.studentrecords;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> listStudents(String name) {
        if (name == null || name.isBlank()) {
            return repository.findAll();
        }
        return repository.searchByName(name.strip());
    }
    public Student getStudentByRegNo(String regNo) {
        return repository.findByRegNo(regNo);
    }
    private void validateStudent(Student student) {
        String regNo = student.getRegNo();
        String name = student.getName();
        String department = student.getDepartment();
        Double cgpa = student.getCgpa();

        if (regNo == null || regNo.isBlank() || regNo.length() > 20) {
            throw new IllegalArgumentException(
                    "Registration number is required and must be at most 20 characters.");
        }

        if (name == null || name.isBlank() || name.length() > 100) {
            throw new IllegalArgumentException(
                    "Name is required and must be at most 100 characters.");
        }

        if (department == null || department.isBlank() || department.length() > 50) {
            throw new IllegalArgumentException(
                    "Department is required and must be at most 50 characters.");
        }

        if (cgpa == null || !Double.isFinite(cgpa)) {
            throw new IllegalArgumentException("Enter a numeric CGPA.");
        }
    }
    public void addStudent(Student student) {
        validateStudent(student);

        if (repository.existsByRegNo(student.getRegNo())) {
            throw new IllegalArgumentException("Registration number already exists.");
        }

        repository.insert(student);
    }
    public void updateStudent(Student student) {
        validateStudent(student);

        int rowsUpdated = repository.update(student);
        if (rowsUpdated == 0 && !repository.existsByRegNo(student.getRegNo())) {
            throw new IllegalArgumentException("Student not found.");
        }
    }
    public void deleteStudent(String regNo) {
        int rowsDeleted = repository.deleteByRegNo(regNo);
        if (rowsDeleted == 0) {
            throw new IllegalArgumentException("Student not found.");
        }
    }

}
