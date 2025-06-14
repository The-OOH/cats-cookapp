package dev.cats.cookapp.dtos.external.response.preferences;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreferenceItem {
    private String  id;
    private String  name;

    private Long    categoryId;
    private Long    ingredientId;

    private boolean selected;
    private String  preferenceType;
}