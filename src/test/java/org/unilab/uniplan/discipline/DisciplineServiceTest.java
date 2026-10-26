package org.unilab.uniplan.discipline;

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
class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    private UUID id;
    private Discipline discipline;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        discipline = new Discipline();
    }

    @Test
    void save_shouldReturnEntity_whenCalled() {
        when(disciplineRepository.save(discipline)).thenReturn(discipline);

        final var result = disciplineService.save(discipline);

        assertThat(result).isSameAs(discipline);
    }

    @Test
    void getAll_shouldReturnListOfEntities_whenCalled() {
        when(disciplineRepository.findAll()).thenReturn(List.of(discipline));

        final var result = disciplineService.getAll();

        assertThat(result).containsExactly(discipline);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));

        final var result = disciplineService.getById(id);

        assertThat(result).contains(discipline);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesNotExist() {
        when(disciplineRepository.findById(id)).thenReturn(Optional.empty());

        final var result = disciplineService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        disciplineService.delete(discipline);

        verify(disciplineRepository).delete(discipline);
    }
}