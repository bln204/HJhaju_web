package com.hjhaju_web.repository;


import com.hjhaju_web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category save(Category category);

}
