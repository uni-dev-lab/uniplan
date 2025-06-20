package org.unilab.uniplan.program;


import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.program.dto.ProgramDto;

@Service
@RequiredArgsConstructor
public class ProgramService{
    public static final String PROGRAM_NOT_FOUND = "Program with ID {0} not found.";

    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;

    @Transactional
    public ProgramDto createProgram(ProgramDto request) {
        final Program program = programMapper.toEntity(request);
        return programMapper.toDto(programRepository.save(program));
    }

    public List<ProgramDto> getAllPrograms() {
       final List<Program> programs = programRepository.findAll();

       return programMapper.toDtoList(programs);
    }

    public Optional<ProgramDto> getProgramById(UUID id) {
        return programRepository.findById(id).map(programMapper::toDto);
    }

    @Transactional
    public Optional<ProgramDto> updateProgram(UUID id, ProgramDto programDto) {
        return programRepository.findById(id)
                                 .map(existingCategory -> {
                                     programMapper.updateEntityFromDto(programDto,
                                                                        existingCategory);

                                     return programMapper.toDto(programRepository.save(
                                         existingCategory));
                                 });
    }

    public void deleteProgram(UUID id) {
        final Program program = programRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException(
                                                MessageFormat.format(
                                                    PROGRAM_NOT_FOUND,
                                                    id
                                                )
                                            ));

        programRepository.delete(program);
    }
}
