package com.scd.studentrecords;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(
            RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String registrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        try {
            registrationService.register(
                username, password, confirmPassword
            );

            return "redirect:/login?registered";

        } catch (IllegalArgumentException ex) {

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("username", username);

            return "register";
        }
    }
}
