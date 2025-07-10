package org.unilab.uniplan.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

    String message,
    int status,
    LocalDateTime timestamp,
    String path
) {

}
