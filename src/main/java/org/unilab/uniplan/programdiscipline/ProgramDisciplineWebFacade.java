package org.unilab.uniplan.programdiscipline;

import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_DISCIPLINE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProgramDisciplineWebFacade {

    private final ProgramDisciplineService programDisciplineService;
    private final ProgramDisciplineMapper programDisciplineMapper;
    private final ProgramDisciplineValidator programDisciplineValidator;

    @Transactional
    public ProgramDisciplineResponseDto createProgramDiscipline(
        final ProgramDisciplineRequestDto request) {

        programDisciplineValidator.validateForCreate(request);

        final ProgramDiscipline programDiscipline = programDisciplineMapper.toEntity(request);
        final ProgramDiscipline saved = programDisciplineService.save(programDiscipline);
        log.info("Created ProgramDiscipline with ID: {}", saved.getId());

        return programDisciplineMapper.toResponseDto(saved);
    }

    @Transactional
    public ProgramDisciplineResponseDto updateProgramDiscipline(
        final UUID disciplineId,
        final UUID programId,
        final ProgramDisciplineRequestDto request) {

        final ProgramDisciplineId id = buildId(disciplineId, programId);

        programDisciplineValidator.validateForUpdate(id, request);

        final ProgramDiscipline programDiscipline = getProgramDisciplineOrThrow(id);

        programDisciplineMapper.updateEntityFromDto(request, programDiscipline);
        final ProgramDiscipline saved = programDisciplineService.save(programDiscipline);
        log.info("Updated ProgramDiscipline with ID: {}", saved.getId());

        return programDisciplineMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public ProgramDisciplineResponseDto getProgramDisciplineById(
        final UUID disciplineId,
        final UUID programId) {

        final ProgramDisciplineId id = buildId(disciplineId, programId);
        final ProgramDiscipline programDiscipline = getProgramDisciplineOrThrow(id);

        return programDisciplineMapper.toResponseDto(programDiscipline);
    }

    @Transactional
    public void deleteProgramDiscipline(
        final UUID disciplineId,
        final UUID programId) {

        final ProgramDisciplineId id = buildId(disciplineId, programId);
        final ProgramDiscipline programDiscipline = getProgramDisciplineOrThrow(id);

        programDisciplineService.delete(programDiscipline);
        log.info("Deleted ProgramDiscipline with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProgramDisciplineResponseDto> getAllProgramDisciplines() {
        return programDisciplineMapper
            .toResponseDtoList(programDisciplineService.getAll());
    }

    private ProgramDiscipline getProgramDisciplineOrThrow(final ProgramDisciplineId id) {
        return programDisciplineService.getById(id)
                                       .orElseThrow(() -> new ResourceNotFoundException(
                                           PROGRAM_DISCIPLINE_NOT_FOUND.getMessage(
                                               String.valueOf(id)
                                           )
                                       ));
    }

    private ProgramDisciplineId buildId(final UUID disciplineId, final UUID programId) {
        return new ProgramDisciplineId(disciplineId, programId);
    }
}
