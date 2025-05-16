package org.unilab.uniplan.programdiscipline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineService {
    private static final String PROGRAM_DISCIPLINE_NOT_FOUND = "Program discipline with ID {0} not found.";

    private final ProgramDisciplineRepository programDisciplineRepository;

    private final ProgramDisciplineMapper programDisciplineMapper;

    @Transactional
    public ProgramDisciplineDto createProgramDiscipline(ProgramDisciplineDto programDisciplineDto){
        final ProgramDiscipline programDiscipline = programDisciplineMapper.toEntity(programDisciplineDto);

        return programDisciplineMapper.toDto(programDisciplineRepository.save(programDiscipline));
    }

    public List<ProgramDisciplineDto> getAllProgramDisciplines(){
        final List<ProgramDiscipline> programDisciplines = programDisciplineRepository.findAll();
        return programDisciplineMapper.toDtos(programDisciplines);
    }

    public Optional<ProgramDisciplineDto> getProgramDisciplineById(ProgramDisciplineId id){
        return programDisciplineRepository.findById(id)
                                          .map(programDisciplineMapper::toDto);
    }

    @Transactional
    public Optional<ProgramDisciplineDto> updateProgramDiscipline(ProgramDisciplineId id, ProgramDisciplineDto programDisciplineDto){
        return programDisciplineRepository.findById(id).map(existingProgramDiscipline -> {
            programDisciplineMapper.updateEntityFromDto(programDisciplineDto,
                                                        existingProgramDiscipline);
            return programDisciplineMapper.toDto(programDisciplineRepository.save(
                existingProgramDiscipline));
        });
    }

    @Transactional
    public void deleteProgramDiscipline(ProgramDisciplineId id){
        final ProgramDiscipline programDiscipline = programDisciplineRepository.findById(id)
                                                                               .orElseThrow(() -> new RuntimeException(
                                                                                   MessageFormat.format(
                                                                                       PROGRAM_DISCIPLINE_NOT_FOUND,
                                                                                       id)));
        programDisciplineRepository.delete(programDiscipline);
    }
}
