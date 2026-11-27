package org.unilab.uniplan.exception;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {

    @Getter
    private final String messageKey; //ex. resource_not_found
    private final Object[] args; //ex. id

    public ResourceNotFoundException(final String messageKey, final Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args.clone();
    }

    public Object[] getArgs() {
        return args.clone();
    }

}