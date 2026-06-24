package org.unilab.uniplan.lector;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.dto.LectorDto;

@ExtendWith(MockitoExtension.class)
class LectorServiceTest {

    @Mock
    private LectorRepository lectorRepository;

    @Mock
    private LectorMapper lectorMapper;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private LectorService lectorService;

    private UUID id;
    private String firstName;
    private String lastName;
    private UUID facultyId;
    private String email;
    private LectorDto lectorDto;
    private LectorDto lectorDtoWithoutFaculty;

    @Mock
    private Lector lector;

    @Mock
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        firstName = "Ivan";
        lastName = "Ivanov";
        email = "i.ivanov@gmail.com";
        lectorDto = new LectorDto(id, facultyId, email, firstName, lastName);
        lectorDtoWithoutFaculty = new LectorDto(id, null, email, firstName, lastName);
    }

    @Test
    void testCreateLectorShouldSaveAndReturnDto() {
        when(lectorMapper.toEntity(lectorDtoWithoutFaculty)).thenReturn(lector);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDtoWithoutFaculty);

        LectorDto result = lectorService.createLector(lectorDtoWithoutFaculty);

        assertEquals(lectorDtoWithoutFaculty, result);
        verifyNoInteractions(facultyRepository);
    }

    @Test
    void testCreateLectorShouldSetFacultyWhenFacultyIdIsNotNull() {
        when(lectorMapper.toEntity(lectorDto)).thenReturn(lector);
        when(facultyRepository.getReferenceById(facultyId)).thenReturn(faculty);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDto);

        LectorDto result = lectorService.createLector(lectorDto);

        assertEquals(lectorDto, result);
        verify(facultyRepository).getReferenceById(facultyId);
        verify(lector).setFaculty(faculty);
    }

    @Test
    void testCreateLectorShouldNotSetFacultyWhenFacultyIdIsNull() {
        when(lectorMapper.toEntity(lectorDtoWithoutFaculty)).thenReturn(lector);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDtoWithoutFaculty);

        LectorDto result = lectorService.createLector(lectorDtoWithoutFaculty);

        assertEquals(lectorDtoWithoutFaculty, result);
        verifyNoInteractions(facultyRepository);
    }

    @Test
    void testGetAllLectorsShouldReturnListOfLectorDtos() {
        List<Lector> entities = List.of(lector);
        List<LectorDto> dtos = List.of(lectorDto);

        when(lectorRepository.findAll()).thenReturn(entities);
        when(lectorMapper.toDtos(entities)).thenReturn(dtos);

        List<LectorDto> result = lectorService.getAllLectors();

        assertEquals(dtos, result);
    }

    @Test
    void testGetLectorByIdShouldReturnLectorDtoIfFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));
        when(lectorMapper.toDto(lector)).thenReturn(lectorDto);

        LectorDto result = lectorService.getLectorById(id);

        assertEquals(lectorDto, result);
    }

    @Test
    void testGetLectorByIdShouldThrowIfNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> lectorService.getLectorById(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testUpdateLectorShouldUpdateAndReturnDtoIfFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));
        doAnswer(invocation -> null).when(lectorMapper).updateEntityFromDto(lectorDtoWithoutFaculty, lector);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDtoWithoutFaculty);

        LectorDto result = lectorService.updateLector(id, lectorDtoWithoutFaculty);

        assertEquals(lectorDtoWithoutFaculty, result);
        verifyNoInteractions(facultyRepository);
    }

    @Test
    void testUpdateLectorShouldSetFacultyWhenFacultyIdIsNotNull() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));
        doAnswer(invocation -> null).when(lectorMapper).updateEntityFromDto(lectorDto, lector);
        when(facultyRepository.getReferenceById(facultyId)).thenReturn(faculty);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDto);

        LectorDto result = lectorService.updateLector(id, lectorDto);

        assertEquals(lectorDto, result);
        verify(facultyRepository).getReferenceById(facultyId);
        verify(lector).setFaculty(faculty);
    }

    @Test
    void testUpdateLectorShouldNotSetFacultyWhenFacultyIdIsNull() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));
        doAnswer(invocation -> null).when(lectorMapper).updateEntityFromDto(lectorDtoWithoutFaculty, lector);
        when(lectorRepository.save(lector)).thenReturn(lector);
        when(lectorMapper.toDto(lector)).thenReturn(lectorDtoWithoutFaculty);

        LectorDto result = lectorService.updateLector(id, lectorDtoWithoutFaculty);

        assertEquals(lectorDtoWithoutFaculty, result);
        verifyNoInteractions(facultyRepository);
    }

    @Test
    void testUpdateLectorShouldThrowIfNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> lectorService.updateLector(id, lectorDto));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testDeleteLectorShouldDeleteLectorIfFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(lector));
        doAnswer(invocation -> null).when(lectorRepository).delete(lector);

        assertDoesNotThrow(() -> lectorService.deleteLector(id));
        verify(lectorRepository).delete(lector);
    }

    @Test
    void testDeleteLectorShouldThrowIfNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> lectorService.deleteLector(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }
}