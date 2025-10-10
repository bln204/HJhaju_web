package com.hjhaju_web.controller.clientController;


import com.hjhaju_web.dto.ChapterDTO;
import com.hjhaju_web.dto.ComicDTO;
import com.hjhaju_web.dto.ComicSuggestionDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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

        Page<Comic> comicPage = comicService.getComic(page, size);

        List<ComicDTO> comicDTOs = comicPage.getContent()
                .stream()
                .map(comicService::toDTO)
                .toList();

        model.addAttribute("comics", comicDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());

        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        return "client/home/show";
    }

    @GetMapping("/search")
    public String searchComics(@RequestParam("query") String query,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               Model model,
                               Authentication authentication) {

        Page<Comic> comicPage = comicService.searchComicsByName(query, page, size);

        model.addAttribute("comics", comicPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());
        model.addAttribute("query", query);

        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        return "client/home/search";
    }


    @GetMapping("/api/search/suggestions")
    @ResponseBody
    public List<ComicSuggestionDTO> getSuggestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return comicService.getSearchSuggestions(query, limit);
    }



    @GetMapping("/the-loai/{slug}")
    public String categoryComics(@PathVariable String slug,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "20") int size,
                                 Model model) {

        model.addAttribute("category", categoryService.findBySlug(slug));

        Page<Comic> comicPage = comicService.findByCategorySlug(slug, page, size);

        model.addAttribute("comics", comicPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comicPage.getTotalPages());

        return "client/home/category";
    }


    @GetMapping("/{slug}")
    public String comicDetails(@PathVariable("slug") String slug, Model model) {
        Comic comic = comicService.findBySlug(slug);
        List<ChapterDTO> chapters = chapterService.getChaptersByComic(comic);

        model.addAttribute("comic", comic);
        model.addAttribute("chapters", chapters);

        return "client/home/detail";
    }


    @GetMapping("/{slug}/chuong-{name}")
    public String chapterDetail(@PathVariable String slug,
                                @PathVariable String name,
                                Model model) {

        Comic comic = comicService.findBySlug(slug);

        Chapter chapter = chapterService.getChapterByComicAndName(comic, name);

        List<Chapter_data> images = chapterDataService.getChapterDataByChapter(chapter);

        List<String> chapterNames = chapterService.getChaptersByComic(comic)
                .stream()
                .map(ChapterDTO::getName)
                .toList();

        model.addAttribute("comic", comic);
        model.addAttribute("chapter", chapter);
        model.addAttribute("images", images);
        model.addAttribute("chapterNames", chapterNames);

        return "client/home/chapterDetail";
    }

}
