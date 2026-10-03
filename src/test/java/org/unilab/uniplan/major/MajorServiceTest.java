package org.unilab.uniplan.major;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

@ExtendWith(MockitoExtension.class)
class MajorServiceTest {

    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private MajorService majorService;

    private Major major;
    private UUID majorId;
    private UUID facultyId;

    @BeforeEach
    void setUp() {
        majorId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        major = new Major();
    }

    @Test
    void saveShouldSaveMajor() {
        majorService.save(major);

        verify(majorRepository).save(major);
    }

    @Test
    void findAllMajorByFacultyIdShouldReturnListOfMajors() {
        final List<Major> majors = List.of(major);

        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of(major));

        final List<Major> result =  majorService.findAllMajorByFacultyId(facultyId);

        assertEquals(majors, result);
        verify(majorRepository).findAllByFacultyId(facultyId);
    }

    @Test
    void findAllMajorByFacultyIdShouldReturnEmptyList() {

        when(majorRepository.findAllByFacultyId(facultyId)).thenReturn(List.of());

        assertTrue(majorService.findAllMajorByFacultyId(facultyId).isEmpty());

        verify(majorRepository).findAllByFacultyId(facultyId);
    }

    @Test
    void getByIdShouldReturnMajorOptionalIfMajorExists() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.of(major));

        Optional<Major> result = majorService.getById(majorId);

        assertEquals(Optional.of(major), result);
        verify(majorRepository).findById(majorId);
    }

    @Test
    void getByIdShouldReturnEmptyOptionalIfMajorDoesNotExist() {
        when(majorRepository.findById(majorId)).thenReturn(Optional.empty());

        final Optional<Major> result = majorService.getById(majorId);

        assertEquals(Optional.empty(), result);
        verify(majorRepository).findById(majorId);
    }

    @Test
    void getAllShouldReturnListOfMajors() {
        final List<Major> majors = List.of(major);
        when(majorRepository.findAll()).thenReturn(List.of(major));

        final List<Major> result = majorService.getAll();

        assertEquals(majors, result);
        verify(majorRepository).findAll();
    }

    @Test
    void deleteShouldDeleteMajor() {
        majorService.delete(major);

        verify(majorRepository).delete(major);
    }

    @Test
    void findAllMajorWithCoursesByFacultyIdShouldReturnListOfMajors() {
        final List<Major> majors = List.of(major);

        when(majorRepository.findAllMajorWithCoursesByFacultyId(facultyId)).thenReturn(List.of(major));

        final List<Major> result = majorService.findAllMajorWithCoursesByFacultyId(facultyId);

        assertEquals(majors, result);
        verify(majorRepository).findAllMajorWithCoursesByFacultyId(facultyId);
    }

    @Test
    void findAllMajorWithCoursesByFacultyIdShouldReturnEmptyList() {
        when(majorRepository.findAllMajorWithCoursesByFacultyId(facultyId)).thenReturn(List.of());

        assertTrue(majorService.findAllMajorWithCoursesByFacultyId(facultyId).isEmpty());

        verify(majorRepository).findAllMajorWithCoursesByFacultyId(facultyId);
    }
}
