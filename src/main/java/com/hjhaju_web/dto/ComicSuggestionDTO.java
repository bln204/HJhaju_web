package com.hjhaju_web.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  ComicSuggestionDTO {
    private String name;

    private String thumbImage;
    private String category;
    private String slug;
}
