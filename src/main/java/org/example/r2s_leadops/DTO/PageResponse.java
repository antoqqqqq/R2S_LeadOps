package org.example.r2s_leadops.DTO;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
        T content,
        int page,
        int size,
        long totalElements,
        long totalPages,
        boolean first,
        boolean last) {

    public static <E, R> PageResponse<List<R>> from(Page<E> page, Function<E, R> mapper) {
        List<R> content = page.getContent().stream().map(mapper).toList();
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
