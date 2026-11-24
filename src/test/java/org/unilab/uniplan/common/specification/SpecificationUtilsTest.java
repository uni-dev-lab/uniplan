package org.unilab.uniplan.common.specification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.lector.Lector;

@ExtendWith(MockitoExtension.class)
class SpecificationUtilsTest {

    private static final String ATTRIBUTE = "firstName";
    private static final char ESCAPE_CHAR = '\\';
    private static final String RAW_VALUE = "Ivan_50%";
    private static final String EXPECTED_PATTERN = "%ivan\\_50\\%%";
    private static final String BLANK_VALUE = " ";
    private static final String BACKSLASH_VALUE = "a\\b";
    private static final String BACKSLASH_PATTERN = "%a\\\\b%";
    private static final String MIXED_VALUE = "\\%";
    private static final String MIXED_PATTERN = "%\\\\\\%%";
    private static final String EXACT_MATCH = "x";
    private static final String RELATED_ENTITY = "faculty";
    private static final String ID = "id";
    private static final String DATE_ATTRIBUTE = "createdAt";
    private static final LocalDateTime DATE_VALUE = LocalDateTime.of(2026, 3, 10, 0, 0);

    @Mock
    private Root<Lector> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<String> path;

    @Mock
    private Expression<String> loweredPath;

    @Mock 
    private Path<LocalDateTime> datePath;

    @Mock
    private Predicate predicate;

    @Captor
    private ArgumentCaptor<String> patternCaptor;

    @Test
    void containsIgnoreCase_shouldEscapeWildcardsAndLowercase_whenValueContainsSpecialChars() {
        doReturn(path).when(root).get(ATTRIBUTE);
        when(cb.lower(path)).thenReturn(loweredPath);
        final var spec = SpecificationUtils.<Lector>containsIgnoreCase(ATTRIBUTE, RAW_VALUE);

        spec.toPredicate(root, query, cb);

        verify(cb).like(eq(loweredPath), patternCaptor.capture(), eq(ESCAPE_CHAR));
        assertThat(patternCaptor.getValue()).isEqualTo(EXPECTED_PATTERN);
    }

    @Test
    void containsIgnoreCase_shouldNotRestrict_whenValueIsAbsent() {
        final var spec = SpecificationUtils.<Lector>containsIgnoreCase(ATTRIBUTE, null);

        final var result = spec.toPredicate(root, query, cb);

        verifyNoInteractions(root, cb);
        assertThat(result).isNull();
    }

    @Test
    void containsIgnoreCase_shouldNotRestrict_whenValueIsBlank() {
        final var spec = SpecificationUtils.<Lector>containsIgnoreCase(ATTRIBUTE, BLANK_VALUE);

        final var result = spec.toPredicate(root, query, cb);

        verifyNoInteractions(root, cb);
        assertThat(result).isNull();
    }

    @Test
    void containsIgnoreCase_shouldEscapeBackslash_whenValueContainsBackslash() {
        doReturn(path).when(root).get(ATTRIBUTE);
        when(cb.lower(path)).thenReturn(loweredPath);
        final var spec = SpecificationUtils.<Lector>containsIgnoreCase(
            ATTRIBUTE, BACKSLASH_VALUE);

        spec.toPredicate(root, query, cb);

        verify(cb).like(eq(loweredPath), patternCaptor.capture(), eq(ESCAPE_CHAR));
        assertThat(patternCaptor.getValue()).isEqualTo(BACKSLASH_PATTERN);
    }

    @Test
    void containsIgnoreCase_shouldEscapeBackslashBeforeOthers_whenMixed() {
        doReturn(path).when(root).get(ATTRIBUTE);
        when(cb.lower(path)).thenReturn(loweredPath);
        final var spec = SpecificationUtils.<Lector>containsIgnoreCase(
            ATTRIBUTE, MIXED_VALUE);

        spec.toPredicate(root, query, cb);

        verify(cb).like(eq(loweredPath), patternCaptor.capture(), eq(ESCAPE_CHAR));
        assertThat(patternCaptor.getValue()).isEqualTo(MIXED_PATTERN);
    }

    @Test
    void equalTo_shouldBuildEqualPredicate_whenValueGiven() {
        doReturn(path).when(root).get(ATTRIBUTE);
        when(cb.equal(path, EXACT_MATCH)).thenReturn(predicate);
        final var spec = SpecificationUtils.<Lector>equalTo(
            ATTRIBUTE, EXACT_MATCH);

        final var result = spec.toPredicate(root, query, cb);

        verify(cb).equal(path, EXACT_MATCH);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void equalTo_shouldNotRestrict_whenValueIsNull() {
        final var spec = SpecificationUtils.<Lector>equalTo(
            ATTRIBUTE, null);

        final var result = spec.toPredicate(root, query, cb);

        verifyNoInteractions(root, cb);
        assertThat(result).isNull();
    }

    @Test
    void relatedIdEquals_shouldCompareRelationId_whenIdGiven() {
        final Path<Object> relationPath = mock();
        final Path<UUID> idPath = mock();
        final UUID id = UUID.randomUUID();
        doReturn(relationPath).when(root).get(RELATED_ENTITY);
        doReturn(idPath).when(relationPath).get(ID);
        when(cb.equal(idPath, id)).thenReturn(predicate);
        final var spec = SpecificationUtils.<Lector>relatedIdEquals(
            RELATED_ENTITY, id);

        final var result = spec.toPredicate(root, query, cb);

        verify(cb).equal(idPath, id);
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void relatedIdEquals_shouldNotRestrict_whenIdIsNull() {
        final var spec = SpecificationUtils.<Lector>relatedIdEquals(
            RELATED_ENTITY, null);

        final var result = spec.toPredicate(root, query, cb);

        verifyNoInteractions(root, cb);
        assertThat(result).isNull();
    }

    @Test
    void dateFrom_shouldUseGreaterThanOrEqual_whenFromGiven() {
        doReturn(datePath).when(root).get(DATE_ATTRIBUTE);
        when(cb.greaterThanOrEqualTo(datePath, DATE_VALUE)).thenReturn(predicate);
        final var spec = SpecificationUtils.<Lector>dateFrom(DATE_ATTRIBUTE, DATE_VALUE);

        final var result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(predicate);
    }

    @Test
    void dateFrom_shouldNotRestrict_whenFromIsNull() {
        final var spec = SpecificationUtils.<Lector>dateFrom(DATE_ATTRIBUTE, null);

        final var result = spec.toPredicate(root, query, cb);

        assertThat(result).isNull();
        verifyNoInteractions(root, cb);
    }

    @Test
    void dateTo_shouldUseStrictLessThan_whenToGiven() {
        doReturn(datePath).when(root).get(DATE_ATTRIBUTE);
        when(cb.lessThan(datePath, DATE_VALUE)).thenReturn(predicate);
        final var spec = SpecificationUtils.<Lector>dateTo(DATE_ATTRIBUTE, DATE_VALUE);

        final var result = spec.toPredicate(root, query, cb);

        assertThat(result).isSameAs(predicate);
    }

    @Test
    void dateTo_shouldNotRestrict_whenToIsNull() {
        final var spec = SpecificationUtils.<Lector>dateTo(DATE_ATTRIBUTE, null);

        final var result = spec.toPredicate(root, query, cb);

        assertThat(result).isNull();
        verifyNoInteractions(root, cb);
    }
}