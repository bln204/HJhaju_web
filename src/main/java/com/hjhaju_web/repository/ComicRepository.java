package com.hjhaju_web.repository;

import com.hjhaju_web.model.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ComicRepository extends JpaRepository<Comic, String> {
    Comic save(Comic comic);

    Page<Comic> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Comic> findById(String id);

    Comic findBySlug(String slug);


    List<Comic> findByNameContainingIgnoreCase(String name);
}
