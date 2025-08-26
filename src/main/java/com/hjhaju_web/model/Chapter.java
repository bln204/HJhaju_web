package com.hjhaju_web.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="chapters")
public class Chapter {
    @Id
    private String id;
    private String name;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    private Comic comic;

    @OneToMany
    private List<Chapter_data> chapter_data;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public List<Chapter_data> getChapter_data() {
        return chapter_data;
    }

    public void setChapter_data(List<Chapter_data> chapter_data) {
        this.chapter_data = chapter_data;
    }
}
