package org.unilab.uniplan.lector;

import static org.unilab.uniplan.common.specification.SpecificationUtils.containsIgnoreCase;
import static org.unilab.uniplan.common.specification.SpecificationUtils.dateFrom;
import static org.unilab.uniplan.common.specification.SpecificationUtils.dateTo;
import static org.unilab.uniplan.common.specification.SpecificationUtils.relatedIdEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.unilab.uniplan.lector.dto.LectorFilterDto;

@UtilityClass
public class LectorSpecifications {

    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String FACULTY = "faculty";
    private static final String CREATED_AT = "createdAt";

    public static final Set<String> ALLOWED_PROPERTIES =
        Set.of(ID, FIRST_NAME, LAST_NAME, EMAIL, CREATED_AT);

    public static Specification<Lector> from(final LectorFilterDto filter) {
        return Specification.allOf(
            containsIgnoreCase(FIRST_NAME, filter.firstName()),
            containsIgnoreCase(LAST_NAME, filter.lastName()),
            containsIgnoreCase(EMAIL, filter.email()),
            relatedIdEquals(FACULTY, filter.facultyId()),
            dateFrom(CREATED_AT, startOfDay(filter.createdFrom())),
            dateTo(CREATED_AT, endOfDay(filter.createdTo()))
        );
    }

    private static LocalDateTime startOfDay(final LocalDate fromDate) {
        return fromDate == null ? null : fromDate.atStartOfDay();
    }

    private static LocalDateTime endOfDay(final LocalDate toDate) {
        return toDate == null ? null : toDate.plusDays(1).atStartOfDay();
    }
}
