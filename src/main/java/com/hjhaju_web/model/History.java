package com.hjhaju_web.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nhiều lịch sử thuộc về 1 user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Nhiều lịch sử thuộc về 1 truyện
    @ManyToOne
    @JoinColumn(name = "comic_id")
    private Comic comic;

    // Chương đang đọc (nếu muốn chi tiết)
    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = true)
    private Chapter chapter;

    // Thời gian đọc gần nhất
    private LocalDateTime lastReadAt;
}

