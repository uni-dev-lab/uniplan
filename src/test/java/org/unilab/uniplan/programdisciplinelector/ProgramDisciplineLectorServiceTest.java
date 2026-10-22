package org.unilab.uniplan.programdisciplinelector;

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
class ProgramDisciplineLectorServiceTest {

    @Mock
    private ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @InjectMocks
    private ProgramDisciplineLectorService programDisciplineLectorService;

    private ProgramDisciplineLectorId programDisciplineLectorId;
    private ProgramDisciplineLector programDisciplineLector;

    @BeforeEach
    void setUp() {
        programDisciplineLectorId = new ProgramDisciplineLectorId(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID());

        programDisciplineLector = new ProgramDisciplineLector();
    }

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        when(programDisciplineLectorRepository.save(programDisciplineLector)).thenReturn(
            programDisciplineLector);

        final var result = programDisciplineLectorService.save(
            programDisciplineLector);

        assertThat(result).isSameAs(programDisciplineLector);
    }

    @Test
    void getAll_shouldReturnAllEntities_whenCalled() {
        when(programDisciplineLectorRepository.findAll()).thenReturn(
            List.of(programDisciplineLector)
        );

        final var result = programDisciplineLectorService.getAll();

        assertThat(result).containsExactly(programDisciplineLector);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(
            Optional.of(programDisciplineLector));

        final var result = programDisciplineLectorService.getById(programDisciplineLectorId);

        assertThat(result).contains(programDisciplineLector);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesntExist() {
        when(programDisciplineLectorRepository.findById(programDisciplineLectorId)).thenReturn(
            Optional.empty());

        final var result = programDisciplineLectorService.getById(programDisciplineLectorId);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        programDisciplineLectorService.delete(programDisciplineLector);

        verify(programDisciplineLectorRepository).delete(programDisciplineLector);
    }
}