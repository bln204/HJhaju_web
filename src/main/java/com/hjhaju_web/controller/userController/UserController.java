package com.hjhaju_web.controller.userController;


import com.hjhaju_web.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String clientHome(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "client/home/show";
    }
}