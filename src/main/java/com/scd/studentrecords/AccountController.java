package com.scd.studentrecords;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account")
    public String account(Principal principal, Model model) {

        model.addAttribute("username", principal.getName());

        return "account";
    }
}
