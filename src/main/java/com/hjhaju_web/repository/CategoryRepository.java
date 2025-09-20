package com.hjhaju_web.repository;


import com.hjhaju_web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category save(Category category);

    List<Category> findAll();

    Category findBySlug(String slug);

}
