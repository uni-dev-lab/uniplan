package org.unilab.uniplan.programdiscipline;

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
class ProgramDisciplineServiceTest {

    @Mock
    private ProgramDisciplineRepository programDisciplineRepository;

    @InjectMocks
    private ProgramDisciplineService programDisciplineService;

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        final ProgramDiscipline saved = mock(ProgramDiscipline.class);
        when(programDisciplineRepository.save(entity)).thenReturn(saved);

        final var result = programDisciplineService.save(entity);

        verify(programDisciplineRepository).save(entity);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void getAll_shouldReturnAllEntities_whenCalled() {
        final List<ProgramDiscipline> entities = mock();
        when(programDisciplineRepository.findAll()).thenReturn(entities);

        final var result = programDisciplineService.getAll();

        verify(programDisciplineRepository).findAll();
        assertThat(result).isSameAs(entities);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        final ProgramDisciplineId id = createId();
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);
        when(programDisciplineRepository.findById(id)).thenReturn(Optional.of(entity));

        final var result = programDisciplineService.getById(id);

        verify(programDisciplineRepository).findById(id);
        assertThat(result).contains(entity);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesntExist() {
        final ProgramDisciplineId id = createId();

        final var result = programDisciplineService.getById(id);

        verify(programDisciplineRepository).findById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        final ProgramDiscipline entity = mock(ProgramDiscipline.class);

        programDisciplineService.delete(entity);

        verify(programDisciplineRepository).delete(entity);
    }

    private ProgramDisciplineId createId() {
        return new ProgramDisciplineId(UUID.randomUUID(), UUID.randomUUID());
    }
}