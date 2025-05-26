package dev.cats.cookapp.dtos.response.recipe;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private String type;
    private String imageUrl;
}
