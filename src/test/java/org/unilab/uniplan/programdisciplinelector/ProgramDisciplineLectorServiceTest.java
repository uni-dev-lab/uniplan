package org.unilab.uniplan.programdisciplinelector;

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
class ProgramDisciplineLectorServiceTest {

    @Mock
    private ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @InjectMocks
    private ProgramDisciplineLectorService programDisciplineLectorService;

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        final ProgramDisciplineLector saved = mock(ProgramDisciplineLector.class);
        when(programDisciplineLectorRepository.save(entity)).thenReturn(saved);

        final var result = programDisciplineLectorService.save(entity);

        verify(programDisciplineLectorRepository).save(entity);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void getAll_shouldReturnAllEntities_whenCalled() {
        final List<ProgramDisciplineLector> entities = mock();
        when(programDisciplineLectorRepository.findAll()).thenReturn(entities);

        final var result = programDisciplineLectorService.getAll();

        verify(programDisciplineLectorRepository).findAll();
        assertThat(result).isSameAs(entities);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        final ProgramDisciplineLectorId id = createId();
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);
        when(programDisciplineLectorRepository.findById(id)).thenReturn(Optional.of(entity));

        final var result = programDisciplineLectorService.getById(id);

        verify(programDisciplineLectorRepository).findById(id);
        assertThat(result).contains(entity);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesntExist() {
        final ProgramDisciplineLectorId id = createId();

        final var result = programDisciplineLectorService.getById(id);

        verify(programDisciplineLectorRepository).findById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        final ProgramDisciplineLector entity = mock(ProgramDisciplineLector.class);

        programDisciplineLectorService.delete(entity);

        verify(programDisciplineLectorRepository).delete(entity);
    }

    private ProgramDisciplineLectorId createId() {
        return new ProgramDisciplineLectorId(UUID.randomUUID(),
                                             UUID.randomUUID(),
                                             UUID.randomUUID());
    }
}