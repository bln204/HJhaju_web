package com.hjhaju_web.service;

import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.repository.ChapterDataRepository;
import com.hjhaju_web.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterDataRepository chapterDataRepository;

    private final ChapterRepository chapterRepository;

    public ChapterService(ChapterDataRepository chapterDataRepository, ChapterRepository chapterRepository) {
        this.chapterDataRepository = chapterDataRepository;
        this.chapterRepository = chapterRepository;
    }

    public List<Chapter_data> findByChapter(String id){
        return this.chapterDataRepository.findByChapterId(id);
    }

    public Chapter getChapterByComicAndName(Comic comic, String name) {
        return chapterRepository.findByComicAndName(comic, name)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }

}
