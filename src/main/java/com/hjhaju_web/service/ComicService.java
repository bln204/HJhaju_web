package com.hjhaju_web.service;

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

@Service
@RequiredArgsConstructor
public class ComicService {
    private final ComicRepository comicRepository;


    public Page<Comic> getComic( int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return comicRepository.findAll(pageable);
    }

    public Page<Comic> searchComics( int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        return comicRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public List<Comic> suggestComics( String name ) {
        return comicRepository.findByNameContainingIgnoreCase( name );
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
}
