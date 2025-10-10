package com.hjhaju_web.repository;

import com.hjhaju_web.model.Comic;
import com.hjhaju_web.model.History;
import com.hjhaju_web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByUser(User user);

    Optional<History> findByUserAndComic(User user, Comic comic);
}
