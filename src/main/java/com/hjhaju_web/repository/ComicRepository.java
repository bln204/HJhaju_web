package com.hjhaju_web.repository;

import com.hjhaju_web.model.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, String> {
    Comic save(Comic comic);

//    Page<Comic> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Comic> findById(String id);

    Comic findBySlug(String slug);

//    Optional<Comic> findBySlug(String Slug);
    List<Comic> findAll();

    List<Comic> findByNameContainingIgnoreCase(String name);
    List<Comic> findByNameContainingIgnoreCase(String query, Pageable pageable);

    List<Comic> findByCategorySlug(String slug);

    void deleteById(String id);
}
