package com.scd.studentrecords;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    private Double parseCgpa(String value) {
        try {
            return value == null ? null : Double.valueOf(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @GetMapping("/students")
    public String listStudents(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("students", service.listStudents(keyword));
        model.addAttribute("keyword", keyword);
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
    public String addStudent(@RequestParam("regNo") String regNo, @RequestParam("name") String name, @RequestParam("department") String department, @RequestParam(value = "cgpa", required = false) String cgpa, Model model, RedirectAttributes redirectAttributes) {

        Student student = new Student(0, regNo.strip(), name.strip(), department.strip(), parseCgpa(cgpa));

        try {
            service.addStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student added successfully.");
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

        model.addAttribute("originalRegNo", regNo);
        model.addAttribute("editing", true);
        return "student-form";
    }

    @RequestMapping(
    value = "/students/{regNo}/update",
    method = {RequestMethod.POST, RequestMethod.PUT}
)
    public String updateStudent(@PathVariable("regNo") String originalRegNo, @RequestParam(value = "regNo", required = false) String regNo, @RequestParam("name") String name, @RequestParam("department") String department, @RequestParam(value = "cgpa", required = false) String cgpa, Model model, RedirectAttributes redirectAttributes) {
        Student student = new Student(0, regNo == null ? "" : regNo.strip(), name.strip(), department.strip(), parseCgpa(cgpa));

    	try {
            service.updateStudent(originalRegNo, student);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully.");
    	}
	catch (IllegalArgumentException ex) {
            model.addAttribute("student", student);
            model.addAttribute("originalRegNo", originalRegNo);
            model.addAttribute("editing", true);
            model.addAttribute("error", ex.getMessage());
            return "student-form";
        }
        return "redirect:/students";
    }

    @RequestMapping(
    value = "/students/{regNo}/delete",
    method = {RequestMethod.POST, RequestMethod.DELETE}
)
    public String deleteStudent(
            @PathVariable("regNo") String regNo,
            RedirectAttributes redirectAttributes) {

        try {
            service.deleteStudent(regNo);
            redirectAttributes.addFlashAttribute(
                "success", "Student deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute(
                "error", ex.getMessage());
        }

        return "redirect:/students";
    }

}
