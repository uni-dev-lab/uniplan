package org.unilab.uniplan.programdisciplinelector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineLectorService {

    private static final String PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND = "Program discipline lector with ID {0} not found.";

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper;

    private final ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @Transactional
    public ProgramDisciplineLectorDto createProgramDisciplineLector(ProgramDisciplineLectorDto programDisciplineLectorDto){
        final ProgramDisciplineLector programDisciplineLector = programDisciplineLectorMapper.toEntity(programDisciplineLectorDto);

        return programDisciplineLectorMapper.toDto(programDisciplineLectorRepository.save(programDisciplineLector));
    }

    public List<ProgramDisciplineLectorDto> getAllProgramDisciplineLectors(){
        final List<ProgramDisciplineLector> programDisciplineLectors = programDisciplineLectorRepository.findAll();
        return programDisciplineLectorMapper.toDtos(programDisciplineLectors);
    }

    public Optional<ProgramDisciplineLectorDto> getProgramDisciplineLectorById(ProgramDisciplineLectorId id){
        return programDisciplineLectorRepository.findById(id)
                                                .map(programDisciplineLectorMapper::toDto);
    }

    @Transactional
    public Optional<ProgramDisciplineLectorDto> updateProgramDisciplineLector(ProgramDisciplineLectorId id, ProgramDisciplineLectorDto programDisciplineLectorDto){
        return programDisciplineLectorRepository.findById(id)
                                                .map(existingProgramDisciplineLector ->{
                                                    programDisciplineLectorMapper
                                                        .updateEntityFromDto(programDisciplineLectorDto,
                                                                             existingProgramDisciplineLector);
                                                    return programDisciplineLectorMapper
                                                        .toDto(programDisciplineLectorRepository
                                                                   .save(existingProgramDisciplineLector));
                                                });
    }

    public void deleteProgramDisciplineLector(ProgramDisciplineLectorId id){
        final ProgramDisciplineLector programDisciplineLector = programDisciplineLectorRepository
            .findById(id)
                .orElseThrow(() -> new RuntimeException(
                    MessageFormat
                        .format(PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND,id)
                ));
        programDisciplineLectorRepository.delete(programDisciplineLector);
    }
}
