package org.unilab.uniplan.program;

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
class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramService programService;

    private UUID id;
    private Program program;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        program = new Program();
    }

    @Test
    void save_shouldReturnSavedEntity_whenCalled() {
        when(programRepository.save(program)).thenReturn(program);

        final var result = programService.save(program);

        assertThat(result).isSameAs(program);
    }

    @Test
    void getAll_shouldReturnAllEntities_whenCalled() {
        when(programRepository.findAll()).thenReturn(List.of(program));

        final var result = programService.getAll();

        assertThat(result).containsExactly(program);
    }

    @Test
    void getById_shouldReturnEntity_whenItExists() {
        when(programRepository.findById(id)).thenReturn(Optional.of(program));

        final var result = programService.getById(id);

        assertThat(result).contains(program);
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityDoesNotExist() {
        when(programRepository.findById(id)).thenReturn(Optional.empty());

        final var result = programService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelete_whenCalled() {
        programService.delete(program);

        verify(programRepository).delete(program);
    }
}