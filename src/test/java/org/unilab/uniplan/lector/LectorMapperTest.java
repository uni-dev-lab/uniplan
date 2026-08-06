package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

class LectorMapperTest {

    private final LectorMapper mapper = new LectorMapperImpl();

    private UUID facultyId;
    private UUID newFacultyId;

    private Lector lector;
    private LectorRequestDto request;
    private LectorRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();
        newFacultyId = UUID.randomUUID();

        final Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        lector = new Lector();
        lector.setId(UUID.randomUUID());
        lector.setFaculty(faculty);
        lector.setEmail("i.ivanov@gmail.com");
        lector.setFirstName("Ivan");
        lector.setLastName("Ivanov");

        request = new LectorRequestDto(facultyId, "i.ivanov@gmail.com", "Ivan", "Ivanov");
        updateRequest = new LectorRequestDto(newFacultyId, "p.petrov@gmail.com", "Petar", "Petrov");
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Lector result = mapper.toEntity(request);

        assertThat(result.getFaculty().getId()).isEqualTo(facultyId);
        assertThat(result.getEmail()).isEqualTo("i.ivanov@gmail.com");
        assertThat(result.getFirstName()).isEqualTo("Ivan");
        assertThat(result.getLastName()).isEqualTo("Ivanov");
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenLectorIsValid() {
        final LectorResponseDto result = mapper.toResponseDto(lector);

        assertThat(result.id()).isEqualTo(lector.getId());
        assertThat(result.facultyId()).isEqualTo(facultyId);
        assertThat(result.email()).isEqualTo("i.ivanov@gmail.com");
        assertThat(result.firstName()).isEqualTo("Ivan");
        assertThat(result.lastName()).isEqualTo("Ivanov");
    }

    @Test
    void updateEntity_shouldReplaceFacultyReference_whenFacultyIdChanges() {
        final Faculty originalFaculty = lector.getFaculty();

        mapper.updateEntity(updateRequest, lector);

        assertThat(lector.getFaculty()).isNotSameAs(originalFaculty);
        assertThat(lector.getFaculty().getId()).isEqualTo(newFacultyId);
    }

    @Test
    void updateEntity_shouldUpdatePersonAndEmailFields_whenRequestDtoIsValid() {
        mapper.updateEntity(updateRequest, lector);

        assertThat(lector.getEmail()).isEqualTo("p.petrov@gmail.com");
        assertThat(lector.getFirstName()).isEqualTo("Petar");
        assertThat(lector.getLastName()).isEqualTo("Petrov");
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final UUID id = lector.getId();

        mapper.updateEntity(updateRequest, lector);

        assertThat(lector.getId()).isEqualTo(id);
    }
}
