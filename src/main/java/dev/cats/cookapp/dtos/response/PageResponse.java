package dev.cats.cookapp.dtos.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> data, Meta meta) {

    public record Meta(int page, int totalPages, long totalItems) { }

    public static <T> PageResponse<T> from(final Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new Meta(
                        page.getNumber() + 1,
                        page.getTotalPages(),
                        page.getTotalElements()
                )
        );
    }
}