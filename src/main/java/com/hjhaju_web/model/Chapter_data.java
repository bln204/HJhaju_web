package com.hjhaju_web.model;

import jakarta.persistence.*;

@Entity
@Table(name="Chapter_data")
public class Chapter_data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String image_page;
    private String image_file;

    @ManyToOne
    private Chapter chapter;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getImage_page() {
        return image_page;
    }

    public void setImage_page(String image_page) {
        this.image_page = image_page;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }
}
