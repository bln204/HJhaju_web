package com.hjhaju_web.controller.clientController;


import com.hjhaju_web.model.Category;
import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.service.CategoryService;
import com.hjhaju_web.service.ChapterDataService;
import com.hjhaju_web.service.ChapterService;
import com.hjhaju_web.service.ComicService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ComicService comicService;
    private final CategoryService categoryService;
    private final ChapterService chapterService;
    private final ChapterDataService chapterDataService;



    @GetMapping("/")
    public String listComics(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                             Authentication authentication) {
//        model.addAttribute("categories" , categoryService.findAll() );

        Page<Comic> comicPage = comicService.getComic(page, size);
        model.addAttribute("comics", comicPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());


        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "client/home/show";
    }

//    @GetMapping("/suggest")
//    public List<Comic> suggestComics( @RequestParam String keyword) {
//        return comicService.suggestComics(keyword);
//    }
//
//    @GetMapping("/search")
//    public String searchComics(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size,
//            @RequestParam(value = "keyword", defaultValue = "") String keyword,
//            Model model) {
//
//        Page<Comic> comics = comicService.searchComics(page, size, keyword);
//
//        model.addAttribute("comics", comics.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", comics.getTotalPages());
//        model.addAttribute("keyword", keyword);
//
//        return "client/home/search";
//    }

    @GetMapping("/the-loai/{slug}")
    public String categoryComics( @PathVariable String slug  ,Model model) {

        model.addAttribute("category" , categoryService.findBySlug(slug));
        model.addAttribute("comics", comicService.findByCategorySlug(slug));

        return "client/home/category";
    }

    @GetMapping("/{slug}")
    public String comicDetails(Model model,@PathVariable("slug") String slug) {
        model.addAttribute("comic", comicService.findBySlug(slug));
        return "client/home/detail";
    }

    @GetMapping("/{slug}/chuong-{name}")
    public String chapterDetail(@PathVariable String slug,
                                @PathVariable String name,
                                Model model) {

        Comic comic = comicService.findBySlug(slug);

        Chapter chapter = chapterService.getChapterByComicAndName(comic, name);

        List<Chapter_data> images = chapterDataService.getChapterDataByChapter(chapter);

        model.addAttribute("comic", comic);
        model.addAttribute("chapter", chapter);
        model.addAttribute("images", images);

        return "client/home/chapterDetail";
    }

}
