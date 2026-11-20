package org.unilab.uniplan.program;

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
class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramService programService;

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        final Program program = mock(Program.class);
        final Program saved = mock(Program.class);
        when(programRepository.save(program)).thenReturn(saved);

        final var result = programService.save(program);

        verify(programRepository).save(program);
        assertThat(result).isSameAs(saved);
    }

    @Test
    void getAll_shouldReturnAllEntities_whenCalled() {
        final List<Program> programs = mock();
        when(programRepository.findAll()).thenReturn(programs);

        final var result = programService.getAll();

        verify(programRepository).findAll();
        assertThat(result).isSameAs(programs);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        final UUID id = UUID.randomUUID();
        final Program program = mock(Program.class);
        when(programRepository.findById(id)).thenReturn(Optional.of(program));

        final var result = programService.getById(id);

        verify(programRepository).findById(id);
        assertThat(result).contains(program);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesNotExist() {
        final UUID id = UUID.randomUUID();

        final var result = programService.getById(id);

        verify(programRepository).findById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        final Program program = mock(Program.class);

        programService.delete(program);

        verify(programRepository).delete(program);
    }
}