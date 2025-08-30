package com.hjhaju_web.controller.clientController;

import com.hjhaju_web.model.User;
import com.hjhaju_web.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("googleLoginUrl", "/oauth2/authorization/google");
        return "client/auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "client/auth/register";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?signupSuccess=true";
        } catch (Exception e) {
            model.addAttribute("signupError", e.getMessage());
            model.addAttribute("user", user);
            return "client/auth/register";
        }
    }
}