package com.scd.studentrecords;

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
    public String listStudents(
            @RequestParam(name = "name", required = false) String name,
            Model model) {
        model.addAttribute("students", service.listStudents(name));
        model.addAttribute("name", name);
        return "students";
    }
}

