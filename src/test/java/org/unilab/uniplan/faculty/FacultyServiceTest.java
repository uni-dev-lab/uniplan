package org.unilab.uniplan.faculty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyService facultyService;
    private UUID id;
    private Faculty facultyEntity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        facultyEntity = new Faculty();
    }

    @Test
    void save_shouldSaveAndReturnEntity() {
        when(facultyRepository.save(facultyEntity)).thenReturn(facultyEntity);

        final var result = facultyService.save(facultyEntity);

        assertEquals(facultyEntity, result);
        verify(facultyRepository).save(facultyEntity);
    }

    @Test
    void findAll_shouldReturnListOfEntities() {
        when(facultyRepository.findAll()).thenReturn(List.of(facultyEntity));

        List<Faculty> faculties = facultyService.getAll();

        assertThat(faculties)
            .hasSize(1)
            .containsExactly(facultyEntity);

        verify(facultyRepository).findAll();
    }

    @Test
    void findById_shouldReturnEntity_whenFacultyExists() {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(facultyEntity));

        Optional<Faculty> faculty = facultyService.getById(id);

        assertThat(faculty)
            .isPresent()
            .contains(facultyEntity);

        verify(facultyRepository).findById(id);
    }

    @Test
    void delete_shouldDeleteEntity_whenFacultyExists(){
        facultyService.delete(facultyEntity);
        
        verify(facultyRepository).delete(facultyEntity);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenFacultyNotFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        final var result = facultyService.getById(id);

        assertThat(result).isEmpty();
    }
}
