package com.scd.studentrecords;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
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

        if (regNo == null || regNo.length() > 20 || !regNo.matches("[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*")) {
            throw new IllegalArgumentException("Registration number must contain letters, numbers or separating hyphens (maximum 20 characters).");
        }
        if (name == null || name.length() > 100 || !name.matches("[A-Za-z]+(?: +[A-Za-z]+)*")) {
            throw new IllegalArgumentException("Name must contain only letters and spaces (maximum 100 characters).");
        }
        if (department == null || !List.of("SE", "CS", "AI", "DS", "CY", "CE").contains(department)) {
            throw new IllegalArgumentException("Select a valid department: SE, CS, AI, DS, CY or CE.");
        }
        if (cgpa == null || !Double.isFinite(cgpa) || cgpa < 0 || cgpa > 4) {
            throw new IllegalArgumentException("CGPA must be a number between 0 and 4.");
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
        updateStudent(student.getRegNo(), student);
    }

    public void updateStudent(String originalRegNo, Student student) {
        validateStudent(student);

        int rowsUpdated;
        try {
            rowsUpdated = repository.update(originalRegNo, student);
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Registration number already exists.", ex);
        }
        if (rowsUpdated == 0 && !repository.existsByRegNo(originalRegNo)) {
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
