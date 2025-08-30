package com.hjhaju_web.repository;

import com.hjhaju_web.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ComicRepository extends JpaRepository<Comic, String> {
    Comic save(Comic comic);

    Optional<Comic> findById(String id);


}
