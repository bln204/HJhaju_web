package com.hjhaju_web.controller.adminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String admin() {
        return "admin/dashboard/show";
    }
}
