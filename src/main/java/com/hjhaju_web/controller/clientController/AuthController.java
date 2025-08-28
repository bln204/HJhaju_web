package com.hjhaju_web.controller.clientController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "client/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "client/auth/register";
    }
}
