package com.hjhaju_web.service;

import com.hjhaju_web.dto.ChapterDTO;
import com.hjhaju_web.dto.ComicDTO;
import com.hjhaju_web.model.Chapter;
import com.hjhaju_web.model.Chapter_data;
import com.hjhaju_web.dto.ComicSuggestionDTO;
import com.hjhaju_web.model.Comic;
import com.hjhaju_web.repository.ChapterDataRepository;
import com.hjhaju_web.repository.ChapterRepository;
import com.hjhaju_web.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterDataRepository chapterDataRepository;
    private final ChapterService chapterService;


    public ComicService(ComicRepository comicRepository, ChapterRepository chapterRepository, ChapterDataRepository chapterDataRepository, ChapterService chapterService) {
        this.comicRepository = comicRepository;
        this.chapterRepository = chapterRepository;
        this.chapterDataRepository = chapterDataRepository;
        this.chapterService = chapterService;
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

    public Page<Comic> searchComicsByName(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return comicRepository.findByNameContainingIgnoreCase(query, pageable);
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

    public Page<Comic> findByCategorySlug(String slug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return comicRepository.findByCategorySlug(slug, pageable);
    }

    public void deleteComic(String id) {
        comicRepository.deleteById(id);
    }

    public ComicDTO toDTO(Comic comic) {
        ComicDTO dto = new ComicDTO();
        dto.setId(comic.getId());
        dto.setName(comic.getName());
        dto.setSlug(comic.getSlug());
        dto.setThumb_image(comic.getThumb_image());
        List<ChapterDTO> latestChapters = chapterService.getLatestTwoChaptersByComic(comic);
        dto.setLatestChapters(latestChapters);
        return dto;
    }
}
