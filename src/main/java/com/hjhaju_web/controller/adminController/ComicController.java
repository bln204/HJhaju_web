package com.hjhaju_web.controller.adminController;

import com.hjhaju_web.service.ComicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ComicController {
    private final ComicService comicService;

    @GetMapping("/comic")
    public String listComics(Model model, String id) {
        model.addAttribute("comics", comicService.findAll());
        return "admin/comic/show";
    }
}
