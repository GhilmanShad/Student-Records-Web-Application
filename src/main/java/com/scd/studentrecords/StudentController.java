package com.scd.studentrecords;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/students")
    public String listStudents(@RequestParam(name = "name", required = false) String name, Model model) {
        model.addAttribute("students", service.listStudents(name));
        model.addAttribute("name", name);
        return "students";
    }

    @GetMapping("/students/{regNo}")
    public String studentDetails(@PathVariable("regNo") String regNo, Model model) {
        Student student = service.getStudentByRegNo(regNo);
        model.addAttribute("student", student);
        model.addAttribute("regNo", regNo);
        return "student-details";
    }

    @GetMapping("/students/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    @PostMapping("/students")
    public String addStudent(@RequestParam("regNo") String regNo, @RequestParam("name") String name, @RequestParam("department") String department, @RequestParam("cgpa") Double cgpa, Model model) {

        Student student = new Student(0, regNo.strip(), name.strip(), department.strip(), cgpa);

        try {
            service.addStudent(student);
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("student", student);
            model.addAttribute("error", ex.getMessage());
            return "student-form";
        }
        return "redirect:/students";
    }

    @GetMapping("/students/{regNo}/edit")
    public String editStudentForm(@PathVariable("regNo") String regNo, Model model) {
        Student student = service.getStudentByRegNo(regNo);
        model.addAttribute("student", student);

        if (student == null) {
            model.addAttribute("regNo", regNo);
            return "student-details";
        }

        model.addAttribute("editing", true);
        return "student-form";
    }

    @PostMapping("/students/{regNo}/update")
    public String updateStudent(@PathVariable("regNo") String regNo, @RequestParam("name") String name, @RequestParam("department") String department, @RequestParam("cgpa") Double cgpa, Model model) {
	Student student = new Student(0, regNo, name.strip(), department.strip(), cgpa);

    	try {
            service.updateStudent(student);
    	}
	catch (IllegalArgumentException ex) {
            model.addAttribute("student", student);
            model.addAttribute("editing", true);
            model.addAttribute("error", ex.getMessage());
            return "student-form";
        }
        return "redirect:/students";
    }

    @PostMapping("/students/{regNo}/delete")
    public String deleteStudent(@PathVariable("regNo") String regNo, Model model) {
        try {
        service.deleteStudent(regNo);
        }
        catch (IllegalArgumentException ex) {
            model.addAttribute("regNo", regNo);
            return "student-details";
        }
        return "redirect:/students";
    }

}
