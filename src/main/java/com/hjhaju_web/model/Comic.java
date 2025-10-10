package com.hjhaju_web.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="comics")
@Data
public class Comic {
    @Id
    private String id;
    private String name;
    private String description;
    private String author;
    private String status;
    private String slug;
    private String thumb_image;

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

    @ManyToMany
    @JoinTable(
            name = "comic_categories",
            joinColumns = @JoinColumn(name = "comic_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> category;

    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapter = new ArrayList<>();

    @OneToMany(mappedBy = "comic")
    private List<Comment> comments;

}
