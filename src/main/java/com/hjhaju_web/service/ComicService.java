package com.hjhaju_web.service;

import com.hjhaju_web.dto.ComicSuggestionDTO;
import com.hjhaju_web.model.Comic;
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
@RequiredArgsConstructor
public class ComicService {
    private final ComicRepository comicRepository;


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

//    public Comic getComicBySlug(String slug) {
//        return comicRepository.findBySlug(slug)
//                .orElseThrow(() -> new RuntimeException("Comic not found"));
//    }
}
