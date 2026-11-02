package org.unilab.uniplan.programdiscipline;

import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.DISCIPLINE_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.discipline.DisciplineRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.program.ProgramRepository;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;

@Component
@RequiredArgsConstructor
public class ProgramDisciplineValidator {

    private final ProgramRepository programRepository;
    private final DisciplineRepository disciplineRepository;
    private final ProgramDisciplineRepository programDisciplineRepository;

    public void validateForCreate(final ProgramDisciplineRequestDto request) {
        validateProgramExists(request.programId());
        validateDisciplineExists(request.disciplineId());
        validateNotAlreadyAssigned(request);
    }

    private void validateNotAlreadyAssigned(final ProgramDisciplineRequestDto request) {
        final ProgramDisciplineId id = new ProgramDisciplineId(
            request.disciplineId(),
            request.programId()
        );
        if (programDisciplineRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                                              "Program discipline with ID "
                                              + id
                                              + " already exists.");
        }
    }

    private void validateProgramExists(final UUID programId) {
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException(
                PROGRAM_NOT_FOUND.getMessage(String.valueOf(programId))
            );
        }
    }

    private void validateDisciplineExists(final UUID disciplineId) {
        if (!disciplineRepository.existsById(disciplineId)) {
            throw new ResourceNotFoundException(
                DISCIPLINE_NOT_FOUND.getMessage(String.valueOf(disciplineId))
            );
        }
    }

}
