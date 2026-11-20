package org.unilab.uniplan.discipline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    @Test
    void save_shouldReturnEntity_whenCalled() {
        final Discipline discipline = mock(Discipline.class);
        final Discipline saved = mock(Discipline.class);
        when(disciplineRepository.save(discipline)).thenReturn(saved);

        final var result = disciplineService.save(discipline);

        verify(disciplineRepository).save(discipline);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void getAll_shouldReturnListOfEntities_whenCalled() {
        final List<Discipline> disciplines = mock();
        when(disciplineRepository.findAll()).thenReturn(disciplines);

        final var result = disciplineService.getAll();

        verify(disciplineRepository).findAll();
        assertThat(result).isSameAs(disciplines);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        final UUID id = UUID.randomUUID();
        final Discipline discipline = mock(Discipline.class);
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));

        final var result = disciplineService.getById(id);

        verify(disciplineRepository).findById(id);
        assertThat(result).contains(discipline);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesNotExist() {
        final UUID id = UUID.randomUUID();

        final var result = disciplineService.getById(id);

        verify(disciplineRepository).findById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        final Discipline discipline = mock(Discipline.class);

        disciplineService.delete(discipline);

        verify(disciplineRepository).delete(discipline);
    }
}