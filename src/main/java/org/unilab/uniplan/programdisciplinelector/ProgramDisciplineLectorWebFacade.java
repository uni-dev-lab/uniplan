package org.unilab.uniplan.programdisciplinelector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProgramDisciplineLectorWebFacade {

    private final ProgramDisciplineLectorService programDisciplineLectorService;
    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper;
    private final ProgramDisciplineLectorValidator programDisciplineLectorValidator;

    @Transactional
    public ProgramDisciplineLectorResponseDto createProgramDisciplineLector(final ProgramDisciplineLectorRequestDto request) {
        programDisciplineLectorValidator.validateForCreate(request);

        final ProgramDisciplineLector programDisciplineLector = programDisciplineLectorMapper.toEntity(
            request);
        final ProgramDisciplineLector saved = programDisciplineLectorService.save(
            programDisciplineLector);
        log.info("Created ProgramDisciplineLector with ID: {}", saved.getId());

        return programDisciplineLectorMapper.toResponseDto(saved);
    }

    @Transactional
    public ProgramDisciplineLectorResponseDto updateProgramDisciplineLector(final UUID lectorId,
                                                                            final UUID programId,
                                                                            final UUID disciplineId,
                                                                            final ProgramDisciplineLectorRequestDto request) {
        final ProgramDisciplineLectorId id = buildProgramDisciplineLectorId(lectorId,
                                                                            programId,
                                                                            disciplineId);

        final ProgramDisciplineLector programDisciplineLector = getProgramDisciplineLectorOrThrow(id);

        programDisciplineLectorMapper.updateEntityFromDto(request, programDisciplineLector);
        final ProgramDisciplineLector saved = programDisciplineLectorService.save(programDisciplineLector);
        log.info("Updated ProgramDisciplineLector with ID: {}", saved.getId());

        return programDisciplineLectorMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public ProgramDisciplineLectorResponseDto getProgramDisciplineLectorById(final UUID lectorId,
                                                                             final UUID programId,
                                                                             final UUID disciplineId) {
        final ProgramDisciplineLectorId id = buildProgramDisciplineLectorId(lectorId,
                                                                            programId,
                                                                            disciplineId);

        final ProgramDisciplineLector programDisciplineLector = getProgramDisciplineLectorOrThrow(id);
        return programDisciplineLectorMapper.toResponseDto(programDisciplineLector);
    }

    @Transactional
    public void deleteProgramDisciplineLector(final UUID lectorId,
                                              final UUID programId,
                                              final UUID disciplineId) {
        final ProgramDisciplineLectorId id = buildProgramDisciplineLectorId(lectorId,
                                                                            programId,
                                                                            disciplineId);

        final ProgramDisciplineLector programDisciplineLector = getProgramDisciplineLectorOrThrow(id);
        programDisciplineLectorService.delete(programDisciplineLector);
        log.info("Deleted ProgramDisciplineLector with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProgramDisciplineLectorResponseDto> getAllProgramDisciplineLectors() {
        return programDisciplineLectorMapper.toResponseDtoList(programDisciplineLectorService.getAll());
    }

    private ProgramDisciplineLector getProgramDisciplineLectorOrThrow(final ProgramDisciplineLectorId id) {
        return programDisciplineLectorService.getById(id)
                                             .orElseThrow(() -> new ResourceNotFoundException(
                                                 PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND.getMessage(
                                                     String.valueOf(id))
                                             ));
    }

    private ProgramDisciplineLectorId buildProgramDisciplineLectorId(final UUID lectorId,
                                                                     final UUID programId,
                                                                     final UUID disciplineId) {
        return new ProgramDisciplineLectorId(lectorId, programId, disciplineId);
    }
}
