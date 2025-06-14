package dev.cats.cookapp.dtos.external.response.preferences;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreferencesResponse {
    private boolean success;
    private PreferencesPayload data;
}