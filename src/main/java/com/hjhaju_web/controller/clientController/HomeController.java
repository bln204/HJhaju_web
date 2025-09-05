package com.hjhaju_web.controller.clientController;


import com.hjhaju_web.model.Comic;
import com.hjhaju_web.service.ComicService;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ComicService comicService;

    @GetMapping("/home")
    public String listComics(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        Page<Comic> comicPage = comicService.getComic(page, size);
        model.addAttribute("comics", comicPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());
        return "client/home/show";
    }

    @GetMapping("/home/suggest")
    public List<Comic> suggestComics( @RequestParam String keyword) {
        return comicService.suggestComics(keyword);
    }

    @GetMapping("/home/search")
    public String searchComics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            Model model) {

        Page<Comic> comics = comicService.searchComics(page, size, keyword);

        model.addAttribute("comics", comics.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comics.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "client/home/search";
    }

}
