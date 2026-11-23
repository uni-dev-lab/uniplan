package org.unilab.uniplan.common.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidDateRangeValidatorTest {

    private static final String CREATED_FROM = "createdFrom";
    private static final String CREATED_TO = "createdTo";

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @ValidDateRange(dateFrom = CREATED_FROM, dateTo = CREATED_TO)
    record DateRangeFilter(LocalDate createdFrom, LocalDate createdTo) {

    }

    @Test
    void isValid_shouldReturnNoViolations_whenFromIsBeforeTo() {
        final var filter = new DateRangeFilter(LocalDate.of(2026, 11, 9),
                                               LocalDate.of(2026, 11, 10));

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations).isEmpty();
    }

    @Test
    void isValid_shouldReturnNoViolations_whenDatesAreSameDay() {
        final var filter = new DateRangeFilter(LocalDate.of(2026, 11, 10),
                                               LocalDate.of(2026, 11, 10));

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations).isEmpty();
    }

    @Test
    void isValid_shouldReturnNoViolations_whenOnlyToSet() {
        final var filter = new DateRangeFilter(null, LocalDate.of(2026, 11, 10));

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations).isEmpty();
    }

    @Test
    void isValid_shouldReturnNoViolations_whenOnlyFromSet() {
        final var filter = new DateRangeFilter(LocalDate.of(2026, 11, 10), null);

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations).isEmpty();
    }

    @Test
    void isValid_shouldReturnNoViolations_whenBothDatesNull() {
        final var filter = new DateRangeFilter(null, null);

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations).isEmpty();
    }

    @Test
    void isValid_shouldReturnViolationOnFromField_whenFromIsAfterTo() {
        final var filter = new DateRangeFilter(LocalDate.of(2026, 11, 20),
                                               LocalDate.of(2026, 11, 10));

        final Set<ConstraintViolation<DateRangeFilter>> violations = validator.validate(filter);

        assertThat(violations)
            .singleElement()
            .satisfies(violation -> assertThat(violation.getPropertyPath())
                .hasToString(CREATED_FROM));
    }
}