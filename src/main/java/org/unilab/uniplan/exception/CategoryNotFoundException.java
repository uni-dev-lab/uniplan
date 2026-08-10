package org.unilab.uniplan.exception;

import java.util.UUID;

public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(final UUID id) {
        super("category_not_found", id);
    }

}