package com.hjhaju_web.controller.adminController;

import com.hjhaju_web.model.Category;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.service.CategoryService;
import com.hjhaju_web.service.ChapterService;
import com.hjhaju_web.service.ComicService;
import com.hjhaju_web.service.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ComicController {
    private final ComicService comicService;
    private final CategoryService categoryService;
    private final UploadFileService uploadFileService;
    private final ChapterService chapterService;

    @GetMapping("/comic")
    public String listComics(Model model, String id) {
        model.addAttribute("comics", comicService.findAll());
        return "admin/comic/show";
    }

    @GetMapping("/details/{slug}")
    public String comicDetails(Model model,@PathVariable("slug") String slug) {
        model.addAttribute("comic", comicService.findBySlug(slug));
        return "admin/comic/details";
    }

    @GetMapping("/comic/add")
    public String addComic(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("comic", new Comic());
        return "admin/comic/createComic";
    }

//    @PostMapping("/comic/add")
//    public String addComic(@ModelAttribute("comic") Comic comic,@RequestParam("file") MultipartFile file ) {
//        comic.setThumb_image(this.uploadFileService.uploadFile(file, "file-upload"));
//        this.comicService.save(comic);
//        return "redirect:/admin/comic";
//    }

    @GetMapping("/details/{slug}/{id}")
    public String getDataChapter(Model model,@PathVariable("id") String id){
        System.out.println(id);
        List<Chapter_data> ChapterDatas = this.chapterService.findByChapter(id);
        model.addAttribute("ChapterDatas", ChapterDatas);
        return "admin/comic/chapterDetails" ;
    }
}
