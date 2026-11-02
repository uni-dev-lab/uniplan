package org.unilab.uniplan.programdiscipline;

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
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgramDisciplineValidatorTest {

    @Mock
    private ProgramDisciplineRepository programDisciplineRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private ProgramDisciplineValidator programDisciplineValidator;

    private ProgramDisciplineRequestDto request;

    @BeforeEach
    void setUp() {
        request = new ProgramDisciplineRequestDto(
            UUID.randomUUID(),
            UUID.randomUUID(),
            (short) 1,
            (short) 1,
            (byte) 1
        );
    }

    @Test
    void validateForCreate_shouldNotThrow_whenValid() {
        final ProgramDisciplineId id = createId(request);
        when(disciplineRepository.existsById(request.disciplineId())).thenReturn(true);
        when(programRepository.existsById(request.programId())).thenReturn(true);

        assertDoesNotThrow(() -> programDisciplineValidator.validateForCreate(request));

        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineRepository).existsById(id);
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFound_whenProgramMissing() {
        assertThatThrownBy(() -> programDisciplineValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.programId().toString());

        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository, never()).existsById(any());
        verify(programDisciplineRepository, never()).existsById(any());
    }

    @Test
    void validateForCreate_shouldThrowResourceNotFound_whenDisciplineMissing() {
        when(programRepository.existsById(request.programId())).thenReturn(true);

        assertThatThrownBy(() -> programDisciplineValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.disciplineId().toString());

        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineRepository, never()).existsById(any());
    }

    @Test
    void validateForCreate_shouldThrowResponseStatus_whenAlreadyAssigned() {
        final ProgramDisciplineId id = createId(request);
        when(programRepository.existsById(request.programId())).thenReturn(true);
        when(disciplineRepository.existsById(request.disciplineId())).thenReturn(true);
        when(programDisciplineRepository.existsById(id)).thenReturn(true);

        assertThatThrownBy(() -> programDisciplineValidator.validateForCreate(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining(id.toString())
            .satisfies(exception -> {
                final ResponseStatusException ex = (ResponseStatusException) exception;
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            });

        verify(programRepository).existsById(request.programId());
        verify(disciplineRepository).existsById(request.disciplineId());
        verify(programDisciplineRepository).existsById(id);
    }

    private ProgramDisciplineId createId(final ProgramDisciplineRequestDto request) {
        return new ProgramDisciplineId(request.disciplineId(), request.programId());
    }
}
