package org.unilab.uniplan.faculty;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
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
import org.unilab.uniplan.faculty.dto.FacultyDto;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private FacultyMapper facultyMapper;

    @InjectMocks
    private FacultyService facultyService;

    private UUID id;
    private UUID universityId;
    private FacultyDto dto;
    private Faculty entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        universityId = UUID.randomUUID();
        dto = new FacultyDto(id, universityId, "Faculty of Science", "Sofia");
        entity = new Faculty();
    }

    @Test
    void testCreateFacultyShouldSaveAndReturnDto() {
        when(facultyMapper.toEntity(dto)).thenReturn(entity);
        when(facultyRepository.save(entity)).thenReturn(entity);
        when(facultyMapper.toDto(entity)).thenReturn(dto);

        FacultyDto result = facultyService.createFaculty(dto);

        assertEquals(dto, result);
    }

    @Test
    void testGetAllFacultiesShouldReturnListOfFacultyDtos() {
        List<Faculty> entities = List.of(entity);
        List<FacultyDto> dtos = List.of(dto);

        when(facultyRepository.findAll()).thenReturn(entities);
        when(facultyMapper.toDtoList(entities)).thenReturn(dtos);

        List<FacultyDto> result = facultyService.getAllFaculties();

        assertEquals(dtos, result);
    }

    @Test
    void testGetFacultyByIdShouldReturnFacultyDtoIfFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(facultyMapper.toDto(entity)).thenReturn(dto);

        Optional<FacultyDto> result = facultyService.getFacultyById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetFacultyByIdShouldReturnEmptyOptionalIfFacultyNotFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        Optional<FacultyDto> result = facultyService.getFacultyById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateFacultyShouldUpdateAndReturnDtoIfFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(facultyMapper).updateEntityFromDto(dto, entity);
        when(facultyRepository.save(entity)).thenReturn(entity);
        when(facultyMapper.toDto(entity)).thenReturn(dto);

        Optional<FacultyDto> result = facultyService.updateFaculty(id, dto);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testUpdateFacultyShouldReturnEmptyOptionalIfNotFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        Optional<FacultyDto> result = facultyService.updateFaculty(id, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteFacultyShouldDeleteFacultyIfFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(facultyRepository).delete(entity);

        assertDoesNotThrow(() -> facultyService.deleteFaculty(id));
        verify(facultyRepository).delete(entity);
    }

    @Test
    void testDeleteFacultyShouldThrowIfNotFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> facultyService.deleteFaculty(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}
