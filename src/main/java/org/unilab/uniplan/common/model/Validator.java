package org.unilab.uniplan.common.model;

public interface Validator<T> {
    void validate(final T entity);
}
