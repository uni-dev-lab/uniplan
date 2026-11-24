package org.unilab.uniplan.common.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class PageResponseDtoTest {

    //Not asserting by values but variables
    @Test
    void from_shouldCopyContentAndMetadata_whenPageHasContent() {
        final int pageNumber = 1;
        final int pageSize = 2;
        final long totalElements = 5;
        final List<String> content = List.of("first", "second");
        final Page<String> page =
            new PageImpl<>(content, PageRequest.of(pageNumber, pageSize), totalElements);

        final var result = PageResponseDto.from(page);

        assertThat(result.content()).isEqualTo(content);
        assertThat(result.page()).isEqualTo(pageNumber);
        assertThat(result.size()).isEqualTo(pageSize);
        assertThat(result.totalElements()).isEqualTo(totalElements);
        assertThat(result.totalPages()).isEqualTo(page.getTotalPages());
    }

    @Test
    void from_shouldReturnEmptyContent_whenPageIsEmpty() {
        final Page<String> page = Page.empty(PageRequest.of(0, 10));

        final var result = PageResponseDto.from(page);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
        assertThat(result.totalPages()).isZero();
    }
}