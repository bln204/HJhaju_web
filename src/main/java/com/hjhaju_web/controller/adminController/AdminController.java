package com.hjhaju_web.controller.adminController;


import com.hjhaju_web.service.UserService;
import org.springframework.ui.Model;
import com.hjhaju_web.service.ComicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ComicService comicService;
    private final UserService userService;

    @GetMapping("/admin/user")
    public String listUsers(Model model) {
        model.addAttribute("users" , userService.findAll());
        return "admin/dashboard/user";
    }

    @GetMapping("/admin/chapter")
    public String chapter() {
        return "admin/dashboard/chapter";
    }

    @GetMapping("/admin")
    public String listComics(Model model) {
        model.addAttribute("comics", comicService.findAll());
        return "admin/dashboard/show";
    }


}
