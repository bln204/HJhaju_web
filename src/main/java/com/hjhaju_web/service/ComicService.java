package com.hjhaju_web.service;

import com.hjhaju_web.model.*;
import com.hjhaju_web.dto.ComicSuggestionDTO;
import com.hjhaju_web.repository.ChapterDataRepository;
import com.hjhaju_web.repository.ChapterRepository;
import com.hjhaju_web.repository.ComicRepository;
import com.hjhaju_web.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterDataRepository chapterDataRepository;
    private final HistoryRepository historyRepository;

    public ComicService(ComicRepository comicRepository
            , ChapterRepository chapterRepository
            , ChapterDataRepository chapterDataRepository
            , HistoryRepository historyRepository) {
        this.comicRepository = comicRepository;
        this.chapterRepository = chapterRepository;
        this.chapterDataRepository = chapterDataRepository;
        this.historyRepository = historyRepository;
    }

    public Page<Comic> getComic(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return comicRepository.findAll(pageable);
    }

    public List<ComicSuggestionDTO> getSearchSuggestions(String query, int limit) {
        return comicRepository.findByNameContainingIgnoreCase(query, PageRequest.of(0, limit))
                .stream()
                .map(comic -> ComicSuggestionDTO.builder()
                        .name(comic.getName())
                        .thumbImage(comic.getThumb_image())
                        .slug(comic.getSlug())
                        .category(
                                comic.getCategory() != null
                                        ? comic.getCategory().stream()
                                        .map(cat -> cat.getName())
                                        .collect(Collectors.joining(", "))
                                        : ""
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }


    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

    public Optional<Comic> findById(String id) {
        return comicRepository.findById(id);
    }

    public Comic findBySlug(String slug) {
        return this.comicRepository.findBySlug(slug);
    }

    public Comic save(Comic comic) {
        return this.comicRepository.save(comic);
    }

    public List<Comic> findByCategorySlug(String slug) {
        return comicRepository.findByCategorySlug(slug);
    }

    public void deleteComic(String id) {
        comicRepository.deleteById(id);
    }

//    public void saveReadingHistory(User user, Comic comic, Chapter chapter) {
//        History history = historyRepository.findByUserAndComic(user, comic)
//                .orElseGet(() -> {
//                    History h = new History();
//                    h.setUser(user);
//                    h.setComic(comic);
//                    return h;
//                });
//
//        history.setChapter(chapter);
//        history.setLastReadAt(LocalDateTime.now());
//        this.historyRepository.save(history);
//    }
}
