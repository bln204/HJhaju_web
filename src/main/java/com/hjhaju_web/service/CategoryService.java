package com.hjhaju_web.service;

import com.hjhaju_web.model.Category;
import com.hjhaju_web.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
}
