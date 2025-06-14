package dev.cats.cookapp.dtos.external.response.preferences;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PreferencesPayload {
    private List<PreferenceItem> diets = List.of();
    private List<PreferenceItem> cuisinePreferences = List.of();
    private List<PreferenceItem> allergies = List.of();
    private List<PreferenceItem> unfavoriteIngredients = List.of();
}