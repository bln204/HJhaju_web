package com.hjhaju_web.service;

import com.hjhaju_web.model.Comic;
import com.hjhaju_web.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComicService {
    @Autowired
    private final ComicRepository comicRepository;


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
