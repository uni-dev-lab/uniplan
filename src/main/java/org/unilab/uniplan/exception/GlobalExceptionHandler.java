package org.unilab.uniplan.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    //Handles resource not found exceptions triggered by element not found by search parameters
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final ResourceNotFoundException ex,
                                                                   final HttpServletRequest request,
                                                                   final Locale locale) {
        log.info(ex.getMessage());

        return new ResponseEntity<>(
            new ErrorResponse(
                messageSource.getMessage("resource_not_found", null, locale),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                request.getRequestURI()
            ),
            HttpStatus.NOT_FOUND
        );
    }

    //Handles validation exceptions triggered by method argument validation failures (e.g. @Valid)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(final HandlerMethodValidationException ex,
                                                                   final HttpServletRequest request,
                                                                   final Locale locale) {
        log.info(ex.getReason());

        return new ResponseEntity<>(
            new ErrorResponse(
                messageSource.getMessage("invalid_data", null, locale),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                request.getRequestURI()
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    // Handles exceptions thrown with specific HTTP status (like 404, 400) via ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(final ResponseStatusException ex,
                                                                       final HttpServletRequest request,
                                                                       final Locale locale) {
        log.info(ex.getReason());

        return new ResponseEntity<>(
            new ErrorResponse(
                messageSource.getMessage("missing_data", null, locale),
                ex.getStatusCode().value(),
                LocalDateTime.now(),
                request.getRequestURI()
            ),
            ex.getStatusCode()
        );
    }

    // Handles type mismatch exceptions (e.g., when a path variable cannot be converted to the expected type)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                            final HttpServletRequest request,
                                                            final Locale locale) {

        final String expectedType = Optional.ofNullable(ex.getRequiredType())
                                      .map(Class::getSimpleName)
                                            .orElse(messageSource.getMessage(
                                                "unknown_type", null,
                                                locale));

        final String message = "Invalid parameter: "
                         + ex.getName()
                         + " should be of type "
                         + expectedType;

        log.info(message);

        return new ResponseEntity<>(new ErrorResponse(
            messageSource.getMessage("error", null,
                                     locale),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            request.getRequestURI()
        ), HttpStatus.BAD_REQUEST);
    }

    // Handles generic runtime exceptions that are not caught by more specific handlers
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException ex,
                                                                final HttpServletRequest request,
                                                                final Locale locale) {

        log.info(ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(
            messageSource.getMessage("server_error", null,
                                     locale),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now(),
            request.getRequestURI()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles any uncaught exceptions, logs the exception type, and returns HTTP 500 with error message
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(final Exception ex, final Locale locale) {

        log.info(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(messageSource.getMessage("error", null, locale));
    }
}