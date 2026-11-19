package org.unilab.uniplan.common.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterableService<T, F> {
    Page<T> getAll(F filter, Pageable pageable);
}
