package com.hjhaju_web.repository;

import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Chapter save(Chapter chapter);

    Optional<Chapter> findById(String id);

    Optional<Chapter> findByComicAndName(Comic comic, String name);

}
