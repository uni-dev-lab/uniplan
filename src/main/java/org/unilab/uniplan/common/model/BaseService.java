package org.unilab.uniplan.common.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseService<T> {

    void save(final T entity);

    List<T> getAll();

    Optional<T> getById(final UUID id);

    void delete(final T entity);
}