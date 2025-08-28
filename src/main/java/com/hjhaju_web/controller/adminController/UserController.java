package com.hjhaju_web.controller.adminController;


import com.hjhaju_web.service.UserService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getHomePage() {
        return "admin/dashboard/show";
    }

    @GetMapping("/user")
    public String listUsers(Model model) {
        model.addAttribute("users" , userService.findAll());
        return "admin/user/show";
    }
}
