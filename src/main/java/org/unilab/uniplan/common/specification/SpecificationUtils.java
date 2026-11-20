package org.unilab.uniplan.common.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@UtilityClass
public class SpecificationUtils {

    private static final char ESCAPE_CHAR = '\\';
    private static final String WILDCARD = "%";
    private static final String ID = "id";

    public static <T> Specification<T> equalTo(final String attribute, final Object value) {
        if (value == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> cb.equal(root.get(attribute), value);
    }

    public static <T> Specification<T> containsIgnoreCase(final String attribute,
                                                          final String value) {
        if (value == null || value.isBlank()) {
            return Specification.unrestricted();
        }
        final String pattern = (WILDCARD + escape(value) + WILDCARD).toLowerCase(Locale.ROOT);
        return (root, query, cb)
            -> cb.like(cb.lower(root.get(attribute)), pattern, ESCAPE_CHAR);
    }

    /*Specifically for _ToOne entities. The entity holds one foreign key.
    There isn't an entity like that whose related id is composite letting us use UUID*/
    public static <T> Specification<T> relatedIdEquals(final String relation, final UUID id) {
        if (id == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb)
            -> cb.equal(root.get(relation).get(ID), id);
    }

    public static <T> Specification<T> dateFrom(final String attribute, final LocalDateTime from) {
        if (from == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb)
            -> cb.greaterThanOrEqualTo(root.get(attribute), from);
    }

    public static <T> Specification<T> dateTo(final String attribute, final LocalDateTime to) {
        if (to == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb)
            -> cb.lessThan(root.get(attribute), to);
    }

    private static String escape(final String value) {
        return value.replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
    }
}
