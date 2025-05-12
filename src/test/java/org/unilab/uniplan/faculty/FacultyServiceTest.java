package org.unilab.uniplan.faculty;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Test
    void testCreateFacultyShouldSaveAndReturnDto() {
        UUID id = UUID.randomUUID();
        UUID universityId = UUID.randomUUID();
        String facultyName = "Faculty of Science";
        String location = "Sofia";

        FacultyDto dto = new FacultyDto(id, universityId, facultyName, location);
        Faculty entity = new Faculty();
        Faculty saved = new Faculty();
        FacultyDto savedDto = new FacultyDto(id, universityId, facultyName, location);

        when(facultyMapper.toEntity(dto)).thenReturn(entity);
        when(facultyRepository.save(entity)).thenReturn(saved);
        when(facultyMapper.toDto(saved)).thenReturn(savedDto);

        FacultyDto result = facultyService.createFaculty(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testGetAllFacultiesShouldReturnListOfFacultyDtos() {
        List<Faculty> entities = List.of(new Faculty());
        List<FacultyDto> dtos = List.of(
            new FacultyDto(UUID.randomUUID(), UUID.randomUUID(), "Arts", "Plovdiv"));

        when(facultyRepository.findAll()).thenReturn(entities);
        when(facultyMapper.toDtoList(entities)).thenReturn(dtos);

        List<FacultyDto> result = facultyService.getAllFaculties();

        assertEquals(dtos, result);
    }

    @Test
    void testGetFacultyByIdShouldReturnFacultyDtoIfFound() {
        UUID id = UUID.randomUUID();
        UUID universityId = UUID.randomUUID();
        Faculty entity = new Faculty();
        FacultyDto dto = new FacultyDto(id, universityId, "Engineering", "Varna");

        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));
        when(facultyMapper.toDto(entity)).thenReturn(dto);

        Optional<FacultyDto> result = facultyService.getFacultyById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetFacultyByIdShouldReturnEmptyOptionalIfFacultyNotFound() {
        UUID id = UUID.randomUUID();

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        Optional<FacultyDto> result = facultyService.getFacultyById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateFacultyShouldUpdateAndReturnDtoIfFound() {
        UUID id = UUID.randomUUID();
        UUID universityId = UUID.randomUUID();
        String updatedName = "Faculty of Medicine";
        String updatedLocation = "Pleven";

        FacultyDto dto = new FacultyDto(id, universityId, updatedName, updatedLocation);
        Faculty existing = new Faculty();
        Faculty updated = new Faculty();
        FacultyDto updatedDto = new FacultyDto(id, universityId, updatedName, updatedLocation);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> null).when(facultyMapper).updateEntityFromDto(dto, existing);
        when(facultyRepository.save(existing)).thenReturn(updated);
        when(facultyMapper.toDto(updated)).thenReturn(updatedDto);

        Optional<FacultyDto> result = facultyService.updateFaculty(id, dto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }

    @Test
    void testUpdateFacultyShouldReturnEmptyOptionalIfNotFound() {
        UUID id = UUID.randomUUID();
        UUID universityId = UUID.randomUUID();
        FacultyDto dto = new FacultyDto(id, universityId, "Law", "Burgas");

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        Optional<FacultyDto> result = facultyService.updateFaculty(id, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteFacultyShouldDeleteFacultyIfFound() {
        UUID id = UUID.randomUUID();
        Faculty entity = new Faculty();

        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(facultyRepository).delete(entity);

        assertDoesNotThrow(() -> facultyService.deleteFaculty(id));
        verify(facultyRepository).delete(entity);
    }

    @Test
    void testDeleteFacultyShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            facultyService.deleteFaculty(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}