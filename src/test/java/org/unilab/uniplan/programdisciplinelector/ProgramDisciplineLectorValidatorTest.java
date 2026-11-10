package org.unilab.uniplan.programdisciplinelector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.discipline.DisciplineRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.lector.LectorRepository;
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineLectorValidatorTest {

    @Mock
    private ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private LectorRepository lectorRepository;

    @InjectMocks
    private ProgramDisciplineLectorValidator programDisciplineLectorValidator;

    private ProgramDisciplineLectorRequestDto request;

    @BeforeEach
    void setUp() {
        request = new ProgramDisciplineLectorRequestDto(UUID.randomUUID(),
                                                        UUID.randomUUID(),
                                                        UUID.randomUUID(),
                                                        LectorType.BOTH);
    }

    @Test
    void validateForCreate_shouldNotThrow_whenValid() {
        final ProgramDisciplineLectorId id = createId(request);
        when(lectorRepository.existsById(request.lectorId())).thenReturn(true);
        when(programRepository.existsById(request.programId())).thenReturn(true);
        when(disciplineRepository.existsById(request.disciplineId())).thenReturn(true);

        assertDoesNotThrow(() -> programDisciplineLectorValidator.validateForCreate(request));

        verify(lectorRepository).existsById(request.lectorId());
        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineLectorRepository).existsById(id);
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenLectorMissing() {
        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.lectorId().toString());

        verify(lectorRepository).existsById(request.lectorId());
        verify(programRepository, never()).existsById(any());
        verify(disciplineRepository, never()).existsById(any());
        verify(programDisciplineLectorRepository, never()).existsById(any());
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenProgramMissing() {
        when(lectorRepository.existsById(request.lectorId())).thenReturn(true);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.programId().toString());

        verify(lectorRepository).existsById(request.lectorId());
        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository, never()).existsById(any());
        verify(programDisciplineLectorRepository, never()).existsById(any());
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenDisciplineMissing() {
        when(lectorRepository.existsById(request.lectorId())).thenReturn(true);
        when(programRepository.existsById(request.programId())).thenReturn(true);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.disciplineId().toString());

        verify(lectorRepository).existsById(request.lectorId());
        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineLectorRepository, never()).existsById(any());
    }

    @Test
    void validateForCreate_shouldThrowResponseStatusException_whenAlreadyAssigned() {
        final ProgramDisciplineLectorId id = createId(request);
        when(lectorRepository.existsById(request.lectorId())).thenReturn(true);
        when(programRepository.existsById(request.programId())).thenReturn(true);
        when(disciplineRepository.existsById(request.disciplineId())).thenReturn(true);
        when(programDisciplineLectorRepository.existsById(id)).thenReturn(true);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining(id.toString())
            .satisfies(exception -> {
                final ResponseStatusException ex = (ResponseStatusException) exception;
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            });

        verify(lectorRepository).existsById(request.lectorId());
        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineLectorRepository).existsById(id);
    }

    private ProgramDisciplineLectorId createId(final ProgramDisciplineLectorRequestDto request) {
        return new ProgramDisciplineLectorId(request.lectorId(),
                                             request.programId(),
                                             request.disciplineId());
    }
}