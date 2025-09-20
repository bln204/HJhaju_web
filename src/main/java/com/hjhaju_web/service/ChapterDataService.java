package com.hjhaju_web.service;


import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.repository.ChapterDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterDataService {
    private final ChapterDataRepository chapterDataRepository;

    public List<Chapter_data> getChapterDataByChapter(Chapter chapter) {
        return chapterDataRepository.findAllByChapterOrderByIdAsc(chapter);
    }

    public List<Chapter_data> getChapterDataByChapterId(String chapterId) {
        return chapterDataRepository.findAllByChapterIdOrderByIdAsc(chapterId);
    }
}
