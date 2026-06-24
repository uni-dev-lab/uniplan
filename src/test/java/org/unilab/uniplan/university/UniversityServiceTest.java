package org.unilab.uniplan.university;

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

@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {
    @Mock
    private UniversityRepository universityRepository;
    @InjectMocks
    private UniversityService universityService;
    private UUID id;
    private University entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new University();
    }

    @Test
    void save_shouldSaveEntity() {
        when(universityRepository.save(entity)).thenReturn(entity);

        universityService.save(entity);

        verify(universityRepository).save(entity);
    }

    @Test
    void findAll_shouldReturnListOfEntities() {
        final List<University> entities = List.of(entity);

        when(universityRepository.findAll()).thenReturn(entities);

        final List<University> result = universityService.getAll();

        assertThat(result).isEqualTo(entities);
        verify(universityRepository).findAll();
    }

    @Test
    void findById_shouldReturnEntity_whenUniversityExists() {
        when(universityRepository.findById(id)).thenReturn(Optional.of(entity));

        final Optional<University> result = universityService.getById(id);

        assertThat(result)
            .isPresent()
            .contains(entity);
    verify(universityRepository).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenUniversityNotFound() {
        when(universityRepository.findById(id)).thenReturn(Optional.empty());

        final var result = universityService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDeleteEntity_whenUniversityExists() {
        universityService.delete(entity);

        verify(universityRepository).delete(entity);
    }
}
