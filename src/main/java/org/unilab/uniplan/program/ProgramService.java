package org.unilab.uniplan.program;


import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.program.dto.ProgramDto;

@Service
@RequiredArgsConstructor
public class ProgramService{

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    @Transactional
    public ProgramDto createProgram(ProgramDto request) {
        final Program program = programMapper.toEntity(request);
        return saveEntityAndConvertToDto(program);
    }

    public List<ProgramDto> getAllPrograms() {
       final List<Program> programs = programRepository.findAll();

       return programMapper.toDtoList(programs);
    }

    public ProgramDto getProgramById(UUID id) {
        return programRepository.findById(id)
                                .map(programMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException(PROGRAM_NOT_FOUND.getMessage(
                                    id.toString())));
    }

    @Transactional
    public ProgramDto updateProgram(UUID id, ProgramDto programDto) {
        return programRepository.findById(id)
                                .map(existingCategory -> updateEntityAndConvertToDto(
                                    programDto,
                                    existingCategory))
                                .orElseThrow(() -> new ResourceNotFoundException(PROGRAM_NOT_FOUND.getMessage(
                                    id.toString())));
    }

    public void deleteProgram(UUID id) {
        final Program program = programRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException(
                                                     PROGRAM_NOT_FOUND.getMessage(id.toString())));

        programRepository.delete(program);
    }

    private ProgramDto updateEntityAndConvertToDto(final ProgramDto dto,
                                                   final Program entity) {
        programMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private ProgramDto saveEntityAndConvertToDto(final Program entity) {
        final Program savedEntity = programRepository.save(entity);
        return programMapper.toDto(savedEntity);
    }
}
