package com.hjhaju_web.controller.clientController;


import com.hjhaju_web.dto.ComicSuggestionDTO;
import com.hjhaju_web.model.*;
import com.hjhaju_web.repository.ComicRepository;
import com.hjhaju_web.service.*;
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
    private final UserService userService;
    private final ComicRepository comicRepository;



    @GetMapping("/")
    public String listComics(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                             Authentication authentication) {

        Page<Comic> comicPage = comicService.getComic(page, size);
        model.addAttribute("comics", comicPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());


        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "client/home/show";
    }


//    @GetMapping("/search")
//    public List<Comic> autocomplete(@RequestParam String keyword) {
//        return comicService.searchByName(keyword);
//    }

    @GetMapping("/api/search/suggestions")
    public List<ComicSuggestionDTO> getSuggestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return comicService.getSearchSuggestions(query, limit);
    }

    @GetMapping("/the-loai/{slug}")
    public String categoryComics( @PathVariable String slug  ,Model model) {

        model.addAttribute("category" , categoryService.findBySlug(slug));
        model.addAttribute("comics", comicService.findByCategorySlug(slug));

        return "client/home/category";
    }

   @GetMapping("/{slug}")
    public String comicDetails(Model model, @PathVariable("slug") String slug) {
        Comic comicOptional = comicService.findBySlug(slug);
        model.addAttribute("comic", comicOptional);
        model.addAttribute("comments", userService.getCommentsByComicId(comicOptional.getId()));
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
//        this.comicService.saveReadingHistory();

        return "client/home/chapterDetail";
    }

    @GetMapping("/test")
    public String test() {
        return "client/home/test";
    }
}
