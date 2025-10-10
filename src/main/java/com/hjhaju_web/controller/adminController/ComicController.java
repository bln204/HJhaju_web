package com.hjhaju_web.controller.adminController;

import com.hjhaju_web.model.Category;
import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    public String comicDetails(Model model, @PathVariable("slug") String slug) {
        Comic comic = this.comicService.findBySlug(slug);
        List<Chapter> chapters = this.chapterService.findByComic(comic);
        model.addAttribute("comic", comic);
        model.addAttribute("chapters", chapters);
        model.addAttribute("categories", comic.getCategory());
        return "admin/comic/comicDetails";
    }

    @GetMapping("/comic/add")
    public String addComic(Model model) {
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("comic", new Comic());
        return "admin/comic/createComic";
    }

    @PostMapping("/comic/add")
    public String addComic(@ModelAttribute("comic") Comic comic, @RequestParam("category") List<Category> categories,
                           @RequestParam("file") MultipartFile file, @RequestParam("slug") String slug,
                           BindingResult bindingResult, Model model) {
        List<Category> listCategories = this.categoryService.findAll();
        Comic newComic = this.comicService.findBySlug(slug);
        if (newComic != null) {
            // báo lỗi vào BindingResult
            bindingResult.rejectValue("slug", "error.comic", "This slug already exists.");

            // load lại form kèm thông báo lỗi
            model.addAttribute("categories", listCategories); // nếu form cần
            return "admin/comic/createComic"; // trả về view form thay vì redirect
        }

        comic.setThumb_image(this.uploadFileService.uploadFile(file, "file-upload"));
        comic.setId(GenerateUUID.generateId());
        comic.setCategory(categories);
        this.comicService.save(comic);

        return "redirect:/admin/comic";
    }

    @GetMapping("/details/{slug}/{id}")
    public String getDataChapter(Model model, @PathVariable("id") String id,@PathVariable("slug") String slug) {
        List<Chapter_data> ChapterDatas = this.chapterService.findByChapter(id);
        model.addAttribute("ChapterDatas", ChapterDatas);
        model.addAttribute("slug", slug);
        return "admin/comic/chapterDetails";
    }

    @PostMapping("/comic/delete/{id}")
    public String deleteComic(@PathVariable("id") String id) {
        String comicId = id;
        this.comicService.deleteComic(comicId);
        return "redirect:/admin/comic";
    }

    @GetMapping("/{slug}/add-chapter")
    public String addChapter(Model model, @PathVariable("slug") String slug) {
        Comic comic = this.comicService.findBySlug(slug);
        String newChap = this.chapterService.newChapter(comic);
        model.addAttribute("newChap", newChap);
        model.addAttribute("comic", comic);
        model.addAttribute("chapter", new Chapter());
        return "admin/comic/createChapter";
    }

    @PostMapping("/{slug}/add-chapter")
    public String saveChapter(@ModelAttribute("chapter") Chapter chapter, @RequestParam("imageFile") List<String> imageFiles, @PathVariable String slug) {
        Comic comic = this.comicService.findBySlug(slug);
        this.chapterService.saveNewChapter(chapter, comic, imageFiles);
        return "redirect:/admin/details/{slug}";
    }

    @PostMapping("/chapter/delete/{id}")
    public String deleteChapter(@PathVariable("id") String id, @RequestParam("slug") String slug) {
        this.chapterService.deleteChapter(id);
        return "redirect:/admin/details/" + slug;
    }
}
