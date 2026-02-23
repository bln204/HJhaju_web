package com.hjhaju_web.controller.clientController;

import com.hjhaju_web.model.Category;
import com.hjhaju_web.repository.CategoryRepository;
import com.hjhaju_web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {
    private final CategoryService categoryService;
    @ModelAttribute("categories")
    public List<Category> getCategories() {
        System.out.println("getCategories");
        return categoryService.findAll();
    }
}
