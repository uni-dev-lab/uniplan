package org.unilab.uniplan.department;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentValidatorTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private DepartmentValidator departmentValidator;

    private UUID facultyId;
    private DepartmentRequestDto request;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();
        request = new DepartmentRequestDto(facultyId, "Engineering");
    }

    @Test
    void validate_shouldPass_whenFacultyExists() {
        when(facultyRepository.existsById(facultyId)).thenReturn(true);

        assertThatCode(() -> departmentValidator.validate(request)).doesNotThrowAnyException();
    }

    @Test
    void validate_shouldThrowResourceNotFoundException_whenFacultyMissing() {
        when(facultyRepository.existsById(facultyId)).thenReturn(false);

        assertThatThrownBy(() -> departmentValidator.validate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(String.valueOf(facultyId));
    }
}
