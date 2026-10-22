package org.unilab.uniplan.programdisciplinelector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.discipline.DisciplineRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.lector.LectorRepository;
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.DISCIPLINE_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.LECTOR_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ProgramDisciplineLectorValidator {

    private final LectorRepository lectorRepository;
    private final ProgramRepository programRepository;
    private final DisciplineRepository disciplineRepository;
    private final ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    public void validateForCreate(final ProgramDisciplineLectorRequestDto request) {
        validateLectorExists(request.lectorId());
        validateProgramExists(request.programId());
        validateDisciplineExists(request.disciplineId());
        validateNotAlreadyAssigned(request);
    }

    void validateNotAlreadyAssigned(final ProgramDisciplineLectorRequestDto request) {
        final ProgramDisciplineLectorId id = new ProgramDisciplineLectorId(
            request.lectorId(),
            request.programId(),
            request.disciplineId()
        );
        if (programDisciplineLectorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                                              "Program discipline lector with ID "
                                              + id
                                              + " already exists.");
        }
    }

    void validateLectorExists(final UUID lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException(
                LECTOR_NOT_FOUND.getMessage(String.valueOf(lectorId))
            );
        }
    }

    void validateProgramExists(final UUID programId) {
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException(
                PROGRAM_NOT_FOUND.getMessage(String.valueOf(programId))
            );
        }
    }

    void validateDisciplineExists(final UUID disciplineId) {
        if (!disciplineRepository.existsById(disciplineId)) {
            throw new ResourceNotFoundException(
                DISCIPLINE_NOT_FOUND.getMessage(String.valueOf(disciplineId))
            );
        }
    }
}
