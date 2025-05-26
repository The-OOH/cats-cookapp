package dev.cats.cookapp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {
    private String id;
    private String name;
}
