package com.hjhaju_web.repository;

import com.hjhaju_web.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByComicIdOrderByCreatedAtDesc(String comicId);
}
