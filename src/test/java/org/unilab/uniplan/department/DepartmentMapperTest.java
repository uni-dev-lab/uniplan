package org.unilab.uniplan.department;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;
import org.unilab.uniplan.faculty.Faculty;

class DepartmentMapperTest {

    private final DepartmentMapper mapper = new DepartmentMapperImpl();

    private UUID facultyId;

    private Department department;
    private Department department2;

    private DepartmentRequestDto request;
    private DepartmentRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();

        final Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        department = new Department(faculty, "Computer Systems");
        department.setId(UUID.randomUUID());

        department2 = new Department(faculty, "Software Engineering");
        department2.setId(UUID.randomUUID());

        request = new DepartmentRequestDto(facultyId, "Computer Systems");

        updateRequest = new DepartmentRequestDto(UUID.randomUUID(), "Software Engineering");
    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Department result = mapper.toEntity(request);

        assertThat(result.getFaculty().getId()).isEqualTo(facultyId);
        assertThat(result.getDepartmentName()).isEqualTo("Computer Systems");
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenDepartmentIsValid() {
        final DepartmentResponseDto result = mapper.toResponseDto(department);

        assertThat(result.id()).isEqualTo(department.getId());
        assertThat(result.facultyId()).isEqualTo(facultyId);
        assertThat(result.departmentName()).isEqualTo("Computer Systems");
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        final var result = mapper.toResponseDtoList(List.of(department, department2));

        assertThat(result)
            .extracting(DepartmentResponseDto::departmentName)
            .containsExactly("Computer Systems", "Software Engineering");
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        final List<DepartmentResponseDto> result = mapper.toResponseDtoList(List.of());

        assertThat(result).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        mapper.updateEntity(updateRequest, department);

        assertThat(department.getDepartmentName()).isEqualTo("Software Engineering");
        assertThat(department.getFaculty().getId()).isEqualTo(updateRequest.facultyId());
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        final UUID id = department.getId();

        mapper.updateEntity(updateRequest, department);

        assertThat(department.getId()).isEqualTo(id);
    }

    @Test
    void toFaculty_shouldMapId_whenFacultyIdIsGiven() {
        final Faculty result = mapper.toFaculty(facultyId);

        assertThat(result.getId()).isEqualTo(facultyId);
    }
}
