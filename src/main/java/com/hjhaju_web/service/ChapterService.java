package com.hjhaju_web.service;

import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.repository.ChapterDataRepository;
import com.hjhaju_web.repository.ChapterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    private final ChapterDataRepository chapterDataRepository;

    public ChapterService(ChapterDataRepository chapterDataRepository) {
        this.chapterDataRepository = chapterDataRepository;
    }

    public List<Chapter_data> findByChapter(String id){
        return this.chapterDataRepository.findByChapterId(id);
    }
}
