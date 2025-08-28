package com.hjhaju_web.service;

import com.hjhaju_web.model.Comic;
import com.hjhaju_web.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComicService {
    @Autowired
    private final ComicRepository comicRepository;


    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

    public Comic findById(String id) {
        return comicRepository.findById(id).orElse(null);
    }

}
