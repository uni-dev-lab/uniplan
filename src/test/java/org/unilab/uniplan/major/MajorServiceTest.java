package org.unilab.uniplan.major;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import org.unilab.uniplan.major.dto.MajorCoursesDto;
import org.unilab.uniplan.major.dto.MajorDto;

@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @InjectMocks
    private MajorService majorService;

    @Mock
    private MajorRepository majorRepository;

    @Mock
    private MajorMapper majorMapper;

    private MajorCoursesDto majorCoursesDto;
    private MajorDto majorDTO;
    private Major major;
    private UUID majorId;
    private UUID facultyId;

    @BeforeEach
    void setUp() {
        majorId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        majorCoursesDto = new MajorCoursesDto(majorId, facultyId, "Informatics", List.of());
        majorDTO = new MajorDto(majorId, facultyId, "Informatics");
        major = new Major();
    }

    @Test
    void createMajorShouldReturnAndSavedMajorDTO() {
        when(majorMapper.toEntity(majorDTO)).thenReturn(major);
        when(majorRepository.save(major)).thenReturn(major);
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        MajorDto result = majorService.createMajor(majorDTO);

        assertNotNull(result);
        assertEquals("Informatics", result.majorName());
        assertEquals(facultyId, result.facultyId());
        verify(majorRepository).save(major);
    }

    @Test
    void findAllMajorByFacultyIdShouldReturnListOfMajors() {
        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of(major));
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        List<MajorDto> result =  majorService.findAllMajorByFacultyId(facultyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Informatics", result.getFirst().majorName());
    }

    @Test
    void findAllMajorByFacultyIdShouldReturnEmptyList() {

        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of());

        assertTrue(majorService.findAllMajorByFacultyId(facultyId).isEmpty());

        verify(majorRepository).findAllByFacultyId(facultyId);
    }

    @Test
    void findAllMajorWithCoursesByFacultyIdShouldReturnListOfMajorCoursesDto() {
        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of(major));
        when(majorMapper.toFullDto(major)).thenReturn(majorCoursesDto);

        List<MajorCoursesDto> result =  majorService.findAllMajorWithCoursesByFacultyId(facultyId);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(1, result.size()),
            () -> assertEquals("Informatics", result.getFirst().majorName()),
            () -> assertEquals(List.of(), result.getFirst().courses())
            );
    }

    @Test
    void findAllMajorWithCoursesByFacultyIdShouldReturnEmptyList() {

        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of());

        assertTrue(majorService.findAllMajorWithCoursesByFacultyId(facultyId).isEmpty());

        verify(majorRepository).findAllByFacultyId(facultyId);
    }

    @Test
    void findMajorByIdShouldReturnMajorDTOIfFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        MajorDto result = majorService.findMajorById(majorId);

        assertEquals("Informatics", result.majorName());
    }

    @Test
    void findMajorByIdShouldReturnEmptyIfNotFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> majorService.findMajorById(majorId));

        assertTrue(exception.getMessage().contains(String.valueOf(majorId)));
    }

    @Test
    void findMajorWithCoursesByIdShouldReturnMajorCoursesDtoIfFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));
        when(majorMapper.toFullDto(major)).thenReturn(majorCoursesDto);

        MajorCoursesDto result = majorService.findMajorWithCoursesById(majorId);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("Informatics", result.majorName()),
            () -> assertEquals(List.of(), result.courses())
        );
    }

    @Test
    void findMajorWithCoursesByIdShouldReturnEmptyIfNotFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> majorService.findMajorWithCoursesById(majorId));

        assertTrue(exception.getMessage().contains(String.valueOf(majorId)));
    }

    @Test
    void findAllShouldReturnListOfMajorDTOs() {
        when(majorRepository.findAll()).thenReturn(List.of(major));
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        List<MajorDto> result = majorService.findAll();

        assertEquals(1, result.size());
        assertEquals("Informatics", result.getFirst().majorName());
    }

    @Test
    void updateMajorShouldReturnUpdatedMajorDTOIfFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));
        doNothing().when(majorMapper).updateEntityFromDto(majorDTO, major);
        when(majorRepository.save(major)).thenReturn(major);
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        MajorDto result = majorService.updateMajor(majorId, majorDTO);

        assertEquals("Informatics", result.majorName());
        verify(majorRepository).save(major);
    }

    @Test
    void updateMajorShouldReturnEmptyIfNotFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> majorService.updateMajor(majorId, majorDTO));

        assertTrue(exception.getMessage().contains(String.valueOf(majorId)));
        verify(majorRepository, never()).save(any());
    }

    @Test
    void deleteMajorShouldDeleteIfExists() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));

        majorService.deleteMajor(majorId);

        verify(majorRepository).delete(major);
    }

    @Test
    void deleteMajorShouldThrowIfNotExists() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                  () -> majorService.deleteMajor(majorId));

        assertTrue(exception.getMessage().contains("Major with ID"));
        verify(majorRepository, never()).delete(any());
    }

    @Test
    void findByIdShouldReturnEntityIfExists() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));
        when(majorMapper.toDto(major)).thenReturn(majorDTO);

        MajorDto result = majorService.findMajorById(majorId);

        assertEquals(majorDTO, result);
        verify(majorMapper).toDto(major);
    }

    @Test
    void findByIdShouldReturnEmptyIfNotFound() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        org.unilab.uniplan.exception.ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, () -> majorService.findMajorById(majorId));

        assertTrue(exception.getMessage().contains(String.valueOf(majorId)));
    }
}
