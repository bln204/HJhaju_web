package com.hjhaju_web.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="category")
@Data
public class Category {
    @Id
    private String id;
    private String name;
    private String slug;

    @ManyToMany
    private List<Comic> comics;
}
