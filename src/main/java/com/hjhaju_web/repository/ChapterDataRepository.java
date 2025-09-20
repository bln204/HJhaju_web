package com.hjhaju_web.repository;

import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterDataRepository extends JpaRepository<Chapter_data, Long> {
    Chapter_data save(Chapter_data chapter_data);

    List<Chapter_data> findByChapterId(String id);

    List<Chapter_data> findAllByChapterOrderByIdAsc(Chapter chapter);

    List<Chapter_data> findAllByChapterIdOrderByIdAsc(String chapterId);

    void deleteByChapter(Chapter chapter);

}
