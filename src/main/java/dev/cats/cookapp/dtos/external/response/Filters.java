package dev.cats.cookapp.dtos.external.response;

import java.util.List;

public record Filters(List<String> diets, List<String> dishTypes, List<String> difficulties) {
}