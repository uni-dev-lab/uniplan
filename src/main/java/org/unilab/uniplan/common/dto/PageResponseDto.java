package org.unilab.uniplan.common.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageResponseDto<T>(
    int page,
    int size,
    long totalElements,
    int totalPages,
    List<T> content
) {
    public static <T> PageResponseDto<T> from(final Page<T> source) {
        return new PageResponseDto<T>(
            source.getNumber(),
            source.getSize(),
            source.getTotalElements(),
            source.getTotalPages(),
            source.getContent()
        );
    }
}
