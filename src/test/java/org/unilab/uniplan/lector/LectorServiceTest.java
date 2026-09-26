package org.unilab.uniplan.lector;


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
class LectorServiceTest {

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private LectorService lectorService;

    private UUID id;
    private Lector entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new Lector();
    }

    @Test
    void save_shouldSaveEntity() {
        when(lectorRepository.save(entity)).thenReturn(entity);

        lectorService.save(entity);

        verify(lectorRepository).save(entity);
    }

    @Test
    void getAll_shouldReturnListOfEntities() {
        final List<Lector> entities = List.of(entity);

        when(lectorRepository.findAll()).thenReturn(entities);

        final List<Lector> result = lectorService.getAll();

        assertThat(result).isEqualTo(entities);
        verify(lectorRepository).findAll();
    }

    @Test
    void getById_shouldReturnEntity_whenLectorExists() {
        when(lectorRepository.findById(id)).thenReturn(Optional.of(entity));

        final Optional<Lector> result = lectorService.getById(id);

        assertThat(result)
            .isPresent()
            .contains(entity);
        verify(lectorRepository).findById(id);
    }

    @Test
    void getById_shouldReturnEmptyOptional_whenLectorNotFound() {
        when(lectorRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<Lector> result = lectorService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDeleteEntity_whenLectorExists() {
        lectorService.delete(entity);

        verify(lectorRepository).delete(entity);
    }
}
