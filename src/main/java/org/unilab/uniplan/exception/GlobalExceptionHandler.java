package org.unilab.uniplan.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    //Handles validation exceptions triggered by method argument validation failures (e.g. @Valid)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HandlerMethodValidationException ex,
                                                                   HttpServletRequest request) {
        return new ResponseEntity<>(
            new ErrorResponse(
                "Validation failed: invalid request data",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getRequestURI()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    // Handles exceptions thrown with specific HTTP status (like 404, 400) via ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex,
                                                                       HttpServletRequest request) {
        return new ResponseEntity<>(
            new ErrorResponse(
                ex.getReason(),
                ex.getStatusCode().value(),
                LocalDateTime.now(),
                request.getRequestURI()
            ),
            ex.getStatusCode()
        );
    }

    // Handles type mismatch exceptions (e.g., when a path variable cannot be converted to the expected type)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {

        String expectedType = Optional.ofNullable(ex.getRequiredType())
                                      .map(Class::getSimpleName)
                                      .orElse("unknown type");

        String message = "Invalid parameter: "
                         + ex.getName()
                         + " should be of type "
                         + expectedType;

        return new ResponseEntity<>(new ErrorResponse(
            message,
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            request.getRequestURI()
        ), HttpStatus.BAD_REQUEST);
    }

    // Handles generic runtime exceptions that are not caught by more specific handlers
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex,
                                                                HttpServletRequest request) {

        return new ResponseEntity<>(new ErrorResponse(
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now(),
            request.getRequestURI()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles any uncaught exceptions, logs the exception type, and returns HTTP 500 with error message
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Error: " + ex.getMessage());
    }
}
