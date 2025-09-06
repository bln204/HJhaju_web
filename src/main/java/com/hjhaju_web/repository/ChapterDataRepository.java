package com.hjhaju_web.repository;

import com.hjhaju_web.model.Chapter_data;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@org.springframework.stereotype.Repository
public interface ChapterDataRepository extends JpaRepository<Chapter_data, Long> {
    Chapter_data save(Chapter_data chapter_data);

    List<Chapter_data> findByChapterId(String id);


}
