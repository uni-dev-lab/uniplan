package org.unilab.uniplan.programdisciplinelector;

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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private UUID lectorId;
    private UUID programId;
    private UUID disciplineId;
    private ProgramDisciplineLectorId programDisciplineLectorId;
    private ProgramDisciplineLectorRequestDto request;

    @BeforeEach
    void setUp() {
        lectorId = UUID.randomUUID();
        programId = UUID.randomUUID();
        disciplineId = UUID.randomUUID();
        programDisciplineLectorId = new ProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        request = new ProgramDisciplineLectorRequestDto(lectorId,
                                                        programId,
                                                        disciplineId,
                                                        LectorType.BOTH);
    }

    @Test
    void validateForCreate_shouldNotThrow_whenValid() {
        when(lectorRepository.existsById(lectorId)).thenReturn(true);
        when(programRepository.existsById(programId)).thenReturn(true);
        when(disciplineRepository.existsById(disciplineId)).thenReturn(true);
        when(programDisciplineLectorRepository.existsById(programDisciplineLectorId)).thenReturn(
            false);

        assertDoesNotThrow(() -> programDisciplineLectorValidator.validateForCreate(request));

        verify(lectorRepository).existsById(lectorId);
        verify(programRepository).existsById(programId);
        verify(disciplineRepository).existsById(disciplineId);
        verify(programDisciplineLectorRepository)
            .existsById(programDisciplineLectorId);
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenLectorMissing() {
        when(lectorRepository.existsById(lectorId)).thenReturn(false);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(lectorId.toString());

        verify(lectorRepository).existsById(lectorId);
        verify(programRepository, never()).existsById(programId);
        verify(disciplineRepository, never()).existsById(disciplineId);
        verify(programDisciplineLectorRepository, never())
            .existsById(programDisciplineLectorId);
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenProgramMissing() {
        when(lectorRepository.existsById(lectorId)).thenReturn(true);
        when(programRepository.existsById(programId)).thenReturn(false);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(programId.toString());

        verify(lectorRepository).existsById(lectorId);
        verify(programRepository).existsById(programId);
        verify(disciplineRepository, never()).existsById(disciplineId);
        verify(programDisciplineLectorRepository, never())
            .existsById(programDisciplineLectorId);
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFoundException_whenDisciplineMissing() {
        when(lectorRepository.existsById(lectorId)).thenReturn(true);
        when(programRepository.existsById(programId)).thenReturn(true);
        when(disciplineRepository.existsById(disciplineId)).thenReturn(false);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(disciplineId.toString());

        verify(lectorRepository).existsById(lectorId);
        verify(programRepository).existsById(programId);
        verify(disciplineRepository).existsById(disciplineId);
        verify(programDisciplineLectorRepository, never())
            .existsById(programDisciplineLectorId);
    }

    @Test
    void validateForCreate_shouldThrowResponseStatusException_whenAlreadyAssigned() {
        when(lectorRepository.existsById(lectorId)).thenReturn(true);
        when(programRepository.existsById(programId)).thenReturn(true);
        when(disciplineRepository.existsById(disciplineId)).thenReturn(true);
        when(programDisciplineLectorRepository.existsById(programDisciplineLectorId)).thenReturn(
            true);

        assertThatThrownBy(() -> programDisciplineLectorValidator.validateForCreate(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining(programDisciplineLectorId.toString())
            .satisfies(exception -> {
                final ResponseStatusException ex = (ResponseStatusException) exception;
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            });

        verify(lectorRepository).existsById(lectorId);
        verify(programRepository).existsById(programId);
        verify(disciplineRepository).existsById(disciplineId);
        verify(programDisciplineLectorRepository)
            .existsById(programDisciplineLectorId);
    }
}
