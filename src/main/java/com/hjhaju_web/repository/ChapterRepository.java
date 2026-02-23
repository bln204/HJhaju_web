package com.hjhaju_web.repository;

import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Chapter save(Chapter chapter);

    Optional<Chapter> findById(String id);

    Optional<Chapter> findByComicAndName(Comic comic, String name);

    List<Chapter> findByComic(Comic comic);

    void deleteByComic(Comic comic);

}
