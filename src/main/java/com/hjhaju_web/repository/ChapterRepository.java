package com.hjhaju_web.repository;

import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    Chapter save(Chapter chapter);

    Optional<Chapter> findById(String id);

    Optional<Chapter> findByComicAndName(Comic comic, String name);

    List<Chapter> findByComicOrderByCreatedAtAsc(Comic comic);

    void deleteByComic(Comic comic);

    List<Chapter> findTop2ByComicOrderByCreatedAtDesc(Comic comic);


    @Query("SELECT c.name FROM Chapter c WHERE c.comic = :comic ORDER BY c.createdAt ASC")
    List<String> findNameByComicOrderByCreatedAtAsc(@Param("comic") Comic comic);

    void deleteById(String id);

}
