package org.unilab.uniplan.program;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.ProgramNotFoundException;
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
                                .orElseThrow(() -> new ProgramNotFoundException(id));
    }

    @Transactional
    public ProgramDto updateProgram(UUID id, ProgramDto programDto) {
        return programRepository.findById(id)
                                .map(existingCategory -> updateEntityAndConvertToDto(
                                    programDto,
                                    existingCategory))
                                .orElseThrow(() -> new ProgramNotFoundException(id));
    }

    public void deleteProgram(UUID id) {
        final Program program = programRepository.findById(id)
                                                 .orElseThrow(() -> new ProgramNotFoundException(id));

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
