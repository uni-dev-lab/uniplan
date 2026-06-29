package org.unilab.uniplan.faculty;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;

@ExtendWith(MockitoExtension.class)
class FacultyWebFacadeTest {

    @Mock
    private FacultyMapper facultyMapper;

    @Mock
    private FacultyService facultyService;

    @InjectMocks
    private FacultyWebFacade facultyWebFacade;

    private UUID id;
    private Faculty entity;
    private FacultyRequestDto requestDto;
    private FacultyResponseDto responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        UUID universityId= UUID.randomUUID();

        entity = new Faculty();
        requestDto = new FacultyRequestDto(universityId, "FMI", "Burgas");
        responseDto = new FacultyResponseDto(id, universityId, "FMI", "Burgas");
    }

    @Test
    void createFaculty_shouldSaveFaculty_whenRequestIsValid() {
        when(facultyMapper.toEntity(requestDto)).thenReturn(entity);

        facultyWebFacade.createFaculty(requestDto);

        final var inOrder = inOrder(facultyMapper, facultyService);

        inOrder.verify(facultyMapper).toEntity(requestDto);
        inOrder.verify(facultyService).save(entity);
    }

    @Test
    void updateFaculty_shouldUpdateFaculty_whenUniversityExists() {
        when(facultyService.getById(id)).thenReturn(Optional.of(entity));

        facultyWebFacade.updateFaculty(id, requestDto);

        verify(facultyMapper).updateEntity(requestDto, entity);
        verify(facultyService).save(entity);
    }

    @Test
    void updateFaculty_shouldThrowResourceNotFoundException_whenFacultyNotFound() {
        when(facultyService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facultyWebFacade.updateFaculty(id, requestDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void getAllFaculties_shouldReturnListOfResponseDtos() {
        final List<Faculty> entities = List.of(entity);
        when(facultyService.getAll()).thenReturn(entities);
        when(facultyMapper.toResponseDtoList(entities)).thenReturn(List.of(responseDto));

        final List<FacultyResponseDto> result = facultyWebFacade.getAllFaculties();

        assertThat(result).containsExactly(responseDto);
    }

    @Test
    void getFacultyById_shouldReturnResponseDto_whenFacultyExists() {
        when(facultyService.getById(id)).thenReturn(Optional.of(entity));
        when(facultyMapper.toResponseDto(entity)).thenReturn(responseDto);

        final FacultyResponseDto result = facultyWebFacade.getFacultyById(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getFacultyById_shouldThrowResourceNotFoundException_whenFacultyNotFound() {
        when(facultyService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facultyWebFacade.getFacultyById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void deleteFaculty_shouldDeleteFaculty_whenFacultyExists() {
        when(facultyService.getById(id)).thenReturn(Optional.of(entity));

        facultyWebFacade.deleteFaculty(id);

        verify(facultyService).delete(entity);
    }

    @Test
    void deleteFaculty_shouldThrowResourceNotFoundException_whenFacultyNotFound() {
        when(facultyService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facultyWebFacade.deleteFaculty(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }
}