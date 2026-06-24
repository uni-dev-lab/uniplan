package org.unilab.uniplan.faculty;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;
import org.unilab.uniplan.university.University;

class FacultyMapperTest {

    private final FacultyMapper mapper = new FacultyMapperImpl();

    private UUID universityId;

    private Faculty faculty;
    private Faculty faculty2;

    private FacultyRequestDto request;
    private FacultyRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        universityId = UUID.randomUUID();

        University university = new University(
            "PU",
            "Burgas",
            (short) 1999,
            "accreditation",
            "www.pu.com"
        );
        university.setId(universityId);

        faculty = new Faculty(university, "FMI", "Burgas");
        faculty.setId(UUID.randomUUID());

        faculty2 = new Faculty(university, "Pedagogika", "Burgas");
        faculty2.setId(UUID.randomUUID());

        request = new FacultyRequestDto(
            universityId, "FMI", "Burgas"
        );

        updateRequest = new FacultyRequestDto(
            universityId, "Pedagogika", "Plovdiv"
        );
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Faculty result = mapper.toEntity(request);

        assertThat(result.getUniversity().getId()).isEqualTo(universityId);
        assertThat(result.getFacultyName()).isEqualTo("FMI");
        assertThat(result.getLocation()).isEqualTo("Burgas");
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenFacultyIsValid() {
        final UUID id = UUID.randomUUID();
        faculty.setId(id);

        final FacultyResponseDto result = mapper.toResponseDto(faculty);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.universityId()).isEqualTo(universityId);
        assertThat(result.location()).isEqualTo("Burgas");
        assertThat(result.facultyName()).isEqualTo("FMI");
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        final var result = mapper.toResponseDtoList(List.of(faculty, faculty2));

        assertThat(result).hasSize(2);

        assertThat(result)
            .extracting(FacultyResponseDto::facultyName)
            .containsExactly("FMI", "Pedagogika");
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final List<FacultyResponseDto> result = mapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        mapper.updateEntity(updateRequest, faculty);

        assertThat(faculty.getFacultyName()).isEqualTo("Pedagogika");
        assertThat(faculty.getLocation()).isEqualTo("Plovdiv");
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final UUID id = faculty.getId();

        mapper.updateEntity(updateRequest, faculty);

        assertThat(faculty.getId()).isEqualTo(id);
    }
}
