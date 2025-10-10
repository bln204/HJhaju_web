package com.hjhaju_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComicDTO {
    private String id;
    private String name;
    private String slug;
    private String thumb_image;
    private List<ChapterDTO> latestChapters;
}
