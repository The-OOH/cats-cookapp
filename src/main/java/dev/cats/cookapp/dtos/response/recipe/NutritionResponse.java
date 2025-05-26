package dev.cats.cookapp.dtos.response.recipe;

import lombok.Data;

@Data
public class NutritionResponse {
    private Long id;
    private Integer calories;
    private Integer fat;
    private Integer protein;
    private Integer carbohydrate;
}
