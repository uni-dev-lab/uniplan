package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.lector.dto.LectorFilterDto;

@ExtendWith(MockitoExtension.class)
class LectorSpecificationsTest {

    private static final String CREATED_AT = "createdAt";
    private static final String FACULTY = "faculty";
    private static final String ID = "id";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final char ESCAPE_CHAR = '\\';
    private static final String TEXT_FIELD = "Field";
    private static final String EXPECTED_PATTERN = "%field%";

    @Mock
    private Root<Lector> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<LocalDateTime> datePath;

    @Mock
    private Path<String> stringPath;

    @Mock
    private Path<Object> relationPath;

    @Mock
    private Path<UUID> idPath;

    @Mock
    private Predicate predicate;

    @Test
    void from_shouldNotRestrict_whenFilterIsEmpty() {
        final LectorFilterDto filter =
            new LectorFilterDto(null, null, null, null, null, null);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        assertThat(result).isNull();
        verifyNoInteractions(root, cb);
    }

    @Test
    void from_shouldUseNextDayMidnightAsUpperBound_whenCreatedToGiven() {
        final LocalDate filterDate = LocalDate.of(2026, 3, 10);
        final LocalDateTime nextDayMidnight = LocalDateTime.of(2026, 3, 11, 0, 0);
        final LectorFilterDto filter =
            new LectorFilterDto(null, null, null, null, null, filterDate);
        doReturn(datePath).when(root).get(CREATED_AT);
        when(cb.lessThan(datePath, nextDayMidnight)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).lessThan(datePath, nextDayMidnight);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void from_shouldUseStartOfDayAsLowerBound_whenCreatedFromGiven() {
        final LocalDate filterDate = LocalDate.of(2026, 3, 10);
        final LocalDateTime startOfFilterDate = LocalDateTime.of(2026, 3, 10, 0, 0);
        final LectorFilterDto filter =
            new LectorFilterDto(null, null, null, null, filterDate, null);
        doReturn(datePath).when(root).get(CREATED_AT);
        when(cb.greaterThanOrEqualTo(datePath, startOfFilterDate)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).greaterThanOrEqualTo(datePath, startOfFilterDate);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void from_shouldFilterByFacultyId_whenFacultyIdGiven() {
        final UUID facultyId = UUID.randomUUID();
        final LectorFilterDto filter =
            new LectorFilterDto(null, null, null, facultyId, null, null);
        doReturn(relationPath).when(root).get(FACULTY);
        doReturn(idPath).when(relationPath).get(ID);
        when(cb.equal(idPath, facultyId)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).equal(idPath, facultyId);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void from_shouldFilterByFirstName_whenFirstNameGiven() {
        final LectorFilterDto filter =
            new LectorFilterDto(TEXT_FIELD, null, null, null, null, null);
        doReturn(stringPath).when(root).get(FIRST_NAME);
        when(cb.lower(stringPath)).thenReturn(stringPath);
        when(cb.like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).lower(stringPath);
        verify(cb).like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void from_shouldFilterByLastName_whenLastNameGiven() {
        final LectorFilterDto filter =
            new LectorFilterDto(null, TEXT_FIELD, null, null, null, null);
        doReturn(stringPath).when(root).get(LAST_NAME);
        when(cb.lower(stringPath)).thenReturn(stringPath);
        when(cb.like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).lower(stringPath);
        verify(cb).like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void from_shouldFilterByEmail_whenEmailGiven() {
        final LectorFilterDto filter =
            new LectorFilterDto(null, null, TEXT_FIELD, null, null, null);
        doReturn(stringPath).when(root).get(EMAIL);
        when(cb.lower(stringPath)).thenReturn(stringPath);
        when(cb.like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR)).thenReturn(predicate);

        final var result = LectorSpecifications.from(filter).toPredicate(root, query, cb);

        verify(cb).lower(stringPath);
        verify(cb).like(stringPath, EXPECTED_PATTERN, ESCAPE_CHAR);
        assertThat(result).isSameAs(predicate);
    }

}