package org.unilab.uniplan.common.specification;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.InvalidSortPropertyException;
import java.util.Set;

@Component
public class SortValidator {
    public void validate(final Pageable pageable, final Set<String> allowedProperties) {
        for (final var order : pageable.getSort()) {
            if (!allowedProperties.contains(order.getProperty())) {
                throw new InvalidSortPropertyException(
                    "Sorting by property '%s' is not allowed".formatted(order.getProperty())
                );
            }
        }
    }
}
