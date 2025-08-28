package com.hjhaju_web.controller.adminController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin() {
        return "admin/dashboard/show";
    }
}
