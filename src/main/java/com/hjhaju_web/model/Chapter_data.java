package com.hjhaju_web.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="Chapter_data")
@Data
public class Chapter_data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String image_page;
    private String image_file;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
