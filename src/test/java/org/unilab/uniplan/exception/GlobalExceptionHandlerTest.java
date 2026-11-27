package org.unilab.uniplan.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);

        handler = new GlobalExceptionHandler(messageSource);
        when(request.getRequestURI()).thenReturn("/api/faculties");
    }

    @Test
    void handleResourceNotFoundException_shouldResolveEnglishMessage_whenKeyExists() {
        final UUID id = UUID.randomUUID();
        final FacultyNotFoundException ex = new FacultyNotFoundException(id);

        final ResponseEntity<ErrorResponse> response =
            handler.handleResourceNotFoundException(ex, request, Locale.ENGLISH);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
            .isNotNull()
            .extracting("message")
            .isEqualTo("The faculty with id " + id + " was not found!");
    }

    @Test
    void handleResourceNotFoundException_shouldResolveBulgarianMessage_whenKeyExists() {
        final UUID id = UUID.randomUUID();
        final FacultyNotFoundException ex = new FacultyNotFoundException(id);

        final ResponseEntity<ErrorResponse> response =
            handler.handleResourceNotFoundException(ex, request, Locale.forLanguageTag("bg"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
            .isNotNull()
            .extracting("message")
            .isEqualTo("Факултетът с id " + id + " не е намерен!");
    }

    @Test
    void handleResourceNotFoundException_shouldUseGenericFallback_whenKeyIsMissing() {
        final UUID id = UUID.randomUUID();
        final ResourceNotFoundException ex =
            new ResourceNotFoundException("nonexistent_not_found_key", id);

        final ResponseEntity<ErrorResponse> response =
            handler.handleResourceNotFoundException(ex, request, Locale.GERMAN);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
            .isNotNull()
            .extracting("message")
            .isEqualTo("The resource was not found!");
    }

    @Test
    void handleValidationException_shouldResolveInvalidData_whenLocaleIsUnsupported() {
        final HandlerMethodValidationException ex = mock(HandlerMethodValidationException.class);

        final ResponseEntity<ErrorResponse> response =
            handler.handleValidationException(ex, request, Locale.GERMAN);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
            .isNotNull()
            .extracting("message")
            .isEqualTo("Invalid data!");
    }
}
