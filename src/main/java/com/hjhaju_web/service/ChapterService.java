package com.hjhaju_web.service;

import com.hjhaju_web.Util.TimeUtils;
import com.hjhaju_web.dto.ChapterDTO;
import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.repository.ChapterDataRepository;
import com.hjhaju_web.repository.ChapterRepository;
import com.hjhaju_web.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterDataRepository chapterDataRepository;
    private final ChapterRepository chapterRepository;
    private final ComicRepository comicRepository;

    public ChapterService(ChapterDataRepository chapterDataRepository, ChapterRepository chapterRepository, ComicRepository comicRepository) {
        this.chapterDataRepository = chapterDataRepository;
        this.chapterRepository = chapterRepository;
        this.comicRepository = comicRepository;
    }

    public List<Chapter> findByComic(Comic comic) {
        return this.chapterRepository.findByComicOrderByCreatedAtAsc(comic);
    }

    public List<Chapter_data> findByChapter(String id) {
        return this.chapterDataRepository.findByChapterId(id);
    }

    public Chapter getChapterByComicAndName(Comic comic, String name) {
        return chapterRepository.findByComicAndName(comic, name)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));
    }

    public List<ChapterDTO> getChaptersByComic(Comic comic) {
        return chapterRepository.findByComic(comic)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ChapterDTO> getLatestTwoChaptersByComic(Comic comic) {
        return chapterRepository.findTop2ByComicOrderByCreatedAtDesc(comic)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ChapterDTO toDTO(Chapter chapter) {
        ChapterDTO dto = new ChapterDTO();
        dto.setId(chapter.getId());
        dto.setName(chapter.getName());
        dto.setTimeAgo(TimeUtils.timeAgo(chapter.getCreatedAt()));
        return dto;
    }


    public void saveNewChapter(Chapter chapter, Comic comic, List<String> imageFiles) {
        chapter.setId(GenerateUUID.generateId());
        chapter.setComic(comic);

        comic.getChapter().add(chapter);

        // chỉ save comic, nhờ cascade sẽ save chapter
        this.comicRepository.save(comic);

        int imagePage = 0;
        for (String imageFile : imageFiles) {
            Chapter_data chapterData = new Chapter_data();
            chapterData.setChapter(chapter);
            chapterData.setImage_file(imageFile);
            imagePage++;
            chapterData.setImage_page(String.valueOf(imagePage));
            this.chapterDataRepository.save(chapterData);
        }
    }


    public String newChapter(Comic comic) {
        List<String> chapters = this.chapterRepository.findNameByComicOrderByCreatedAtAsc(comic);
        if (chapters == null || chapters.isEmpty()) {
            return "1";
        }
        int last = Integer.parseInt(chapters.get(chapters.size() - 1));
        return String.valueOf(last + 1);
    }

    public void deleteChapter(String id) {
        this.chapterRepository.deleteById(id);
    }
}
