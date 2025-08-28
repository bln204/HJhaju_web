package com.hjhaju_web.controller.userController;


import com.hjhaju_web.service.user.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}