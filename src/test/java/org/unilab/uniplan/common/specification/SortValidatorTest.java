package org.unilab.uniplan.common.specification;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.unilab.uniplan.exception.InvalidSortPropertyException;

class SortValidatorTest {

    private static final Set<String> ALLOWED = Set.of("firstName", "id");
    private static final String FIRST_SORT = "firstName";
    private static final String SECOND_SORT = "id";
    private static final String BAD_FIELD = "badField";

    private final SortValidator sortValidator = new SortValidator();

    @Test
    void validate_shouldPass_whenAllPropertiesAllowed() {
        final PageRequest pageable =
            PageRequest.of(0, 10, Sort.by(FIRST_SORT, SECOND_SORT));

        assertThatCode(() -> sortValidator.validate(pageable, ALLOWED))
            .doesNotThrowAnyException();
    }

    @Test
    void validate_shouldPass_whenPageableIsUnsorted() {
        final PageRequest pageable = PageRequest.of(0, 10);

        assertThatCode(() -> sortValidator.validate(pageable, ALLOWED))
            .doesNotThrowAnyException();
    }

    @Test
    void validate_shouldThrowInvalidSortProperty_whenPropertyNotAllowed() {
        final PageRequest pageable =
            PageRequest.of(0, 10, Sort.by(BAD_FIELD));

        assertThatThrownBy(() -> sortValidator.validate(pageable, ALLOWED))
            .isInstanceOf(InvalidSortPropertyException.class)
            .hasMessageContaining(BAD_FIELD);
    }

    @Test
    void validate_shouldThrowInvalidSortProperty_whenOneOfManyPropertiesNotAllowed() {
        final PageRequest pageable =
            PageRequest.of(0, 10,
                           Sort.by(FIRST_SORT, SECOND_SORT, BAD_FIELD));

        assertThatThrownBy(() -> sortValidator.validate(pageable, ALLOWED))
            .isInstanceOf(InvalidSortPropertyException.class)
            .hasMessageContaining(BAD_FIELD);
    }
}
