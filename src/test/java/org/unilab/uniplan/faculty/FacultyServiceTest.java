package org.unilab.uniplan.faculty;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.faculty.FacultyService;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyService facultyService;
    private UUID id;
    private Faculty entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new Faculty();
    }

    @Test
    void save_shouldSaveEntity() {
        when(facultyRepository.save(entity)).thenReturn(entity);

        facultyService.save(entity);

        verify(facultyRepository).save(entity);
    }

    @Test
    void findAll_shouldReturnListOfEntities() {
        final List<Faculty> entities = List.of(entity);

        when(facultyRepository.findAll()).thenReturn(entities);

        final List<Faculty> result = facultyService.getAll();

        assertThat(result).isEqualTo(entities);
        verify(facultyRepository).findAll();
    }

    @Test
    void findById_shouldReturnEntity_whenFacultyExists() {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(entity));

        final Optional<Faculty> result = facultyService.getById(id);

        assertThat(result)
            .isPresent()
            .contains(entity);
        verify(facultyRepository).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenFacultyNotFound() {
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<Faculty> result = facultyService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDeleteEntity_whenFacultyExists() {
        facultyService.delete(entity);

        verify(facultyRepository).delete(entity);
    }
}
