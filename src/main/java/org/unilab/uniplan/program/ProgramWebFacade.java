package org.unilab.uniplan.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProgramWebFacade {

    private final ProgramService programService;
    private final ProgramMapper programMapper;
    private final ProgramValidator programValidator;

    @Transactional
    public ProgramResponseDto createProgram(final ProgramRequestDto request) {
        programValidator.validateForCreate(request);

        final Program program = programMapper.toEntity(request);
        final Program saved = programService.save(program);
        log.info("Created Program with ID: {}", saved.getId());

        return programMapper.toResponseDto(saved);
    }

    @Transactional
    public ProgramResponseDto updateProgram(final UUID id, final ProgramRequestDto request) {
        programValidator.validateForUpdate(id, request);

        final Program program = getProgramOrThrow(id);

        programMapper.updateEntityFromDto(request, program);
        final Program saved = programService.save(program);
        log.info("Updated Program with ID: {}", saved.getId());

        return programMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public ProgramResponseDto getProgramById(final UUID id) {
        final Program program = getProgramOrThrow(id);

        return programMapper.toResponseDto(program);
    }

    @Transactional(readOnly = true)
    public List<ProgramResponseDto> getAllPrograms() {
        return programMapper.toResponseDtoList(programService.getAll());
    }

    @Transactional
    public void deleteProgram(final UUID id) {
        final Program program = getProgramOrThrow(id);

        programService.delete(program);
        log.info("Deleted Program with ID: {}", id);
    }

    private Program getProgramOrThrow(final UUID id) {
        return programService.getById(id)
                             .orElseThrow(() -> new ResourceNotFoundException(
                                 PROGRAM_NOT_FOUND.getMessage(String.valueOf(id))
                             ));
    }
}
