package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

class LectorMapperTest {

    private final LectorMapper lectorMapper = new LectorMapperImpl();

    @Test
    void toEntity_shouldMapFieldsAndIgnoreId_whenCalled() {
        final LectorRequestDto request = createRequest();

        final Lector result = lectorMapper.toEntity(request);

        assertAll(
            () -> assertThat(result.getFaculty().getId()).isEqualTo(request.facultyId()),
            () -> assertThat(result.getEmail()).isEqualTo(request.email()),
            () -> assertThat(result.getFirstName()).isEqualTo(request.firstName()),
            () -> assertThat(result.getLastName()).isEqualTo(request.lastName()),
            () -> assertThat(result.getId()).isNull()
        );
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenCalled() {
        final Lector lector = createLector(UUID.randomUUID(), UUID.randomUUID());

        final LectorResponseDto result = lectorMapper.toResponseDto(lector);

        assertAll(
            () -> assertThat(result.id()).isEqualTo(lector.getId()),
            () -> assertThat(result.facultyId()).isEqualTo(lector.getFaculty().getId()),
            () -> assertThat(result.email()).isEqualTo(lector.getEmail()),
            () -> assertThat(result.firstName()).isEqualTo(lector.getFirstName()),
            () -> assertThat(result.lastName()).isEqualTo(lector.getLastName())
        );
    }

    @Test
    void toResponseDto_shouldHaveNullFacultyId_whenLectorHasNoFaculty() {
        final Lector lector = createLector(UUID.randomUUID(), UUID.randomUUID());
        lector.setFaculty(null);

        final LectorResponseDto result = lectorMapper.toResponseDto(lector);

        assertThat(result.facultyId()).isNull();
    }

    @Test
    void toResponseDtoList_shouldMapEveryElement_whenCalled() {
        final Lector first = createLector(UUID.randomUUID(), UUID.randomUUID());
        final Lector second = createLector(UUID.randomUUID(), UUID.randomUUID());

        final List<LectorResponseDto> result =
            lectorMapper.toResponseDtoList(List.of(first, second));

        assertThat(result).extracting(LectorResponseDto::id)
                          .containsExactly(first.getId(), second.getId());
    }

    @Test
    void toResponseDtoList_shouldBeEmpty_whenListIsEmpty() {
        final List<LectorResponseDto> result = lectorMapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateFieldsAndReplaceFacultyAndKeepId_whenCalled() {
        final UUID id = UUID.randomUUID();
        final Lector lector = createLector(id, UUID.randomUUID());
        final Faculty originalFaculty = lector.getFaculty();
        final LectorRequestDto request = createRequest();

        lectorMapper.updateEntity(request, lector);

        assertAll(
            () -> assertThat(lector.getId()).isEqualTo(id),
            () -> assertThat(lector.getFaculty()).isNotSameAs(originalFaculty),
            () -> assertThat(lector.getFaculty().getId()).isEqualTo(request.facultyId()),
            () -> assertThat(lector.getEmail()).isEqualTo(request.email()),
            () -> assertThat(lector.getFirstName()).isEqualTo(request.firstName()),
            () -> assertThat(lector.getLastName()).isEqualTo(request.lastName())
        );
    }

    @Test
    void toFaculty_shouldReturnFacultyWithId_whenIdIsGiven() {
        final UUID facultyId = UUID.randomUUID();

        final Faculty result = lectorMapper.toFaculty(facultyId);

        assertThat(result.getId()).isEqualTo(facultyId);
    }

    @Test
    void toFaculty_shouldReturnNull_whenIdIsNull() {
        final Faculty result = lectorMapper.toFaculty(null);

        assertThat(result).isNull();
    }

    private Lector createLector(final UUID id, final UUID facultyId) {
        final Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        final Lector lector = new Lector();
        lector.setId(id);
        lector.setFaculty(faculty);
        lector.setEmail("g.georgiev@gmail.com");
        lector.setFirstName("Georgi");
        lector.setLastName("Georgiev");
        return lector;
    }

    private LectorRequestDto createRequest() {
        return new LectorRequestDto(UUID.randomUUID(), "p.petrov@gmail.com", "Petar", "Petrov");
    }
}
