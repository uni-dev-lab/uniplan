package org.unilab.uniplan.programdiscipline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
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
        ProgramDiscipline programDiscipline = programDisciplineRepository.findByProgramId(id);
        return Optional.ofNullable(programDisciplineMapper.toDto(programDiscipline));
    }

    @Transactional
    public Optional<ProgramDisciplineDto> updateProgramDiscipline(ProgramDisciplineId id, ProgramDisciplineDto programDisciplineDto){
       return Optional.ofNullable(programDisciplineRepository.findByProgramId(id))
                                                                .map(existingProgramDiscipline ->{
                                                                   programDisciplineMapper.updateEntityFromDto(programDisciplineDto,
                                                                                                           existingProgramDiscipline);
                                                                   return programDisciplineMapper.toDto(programDisciplineRepository.save(
                                                                       existingProgramDiscipline));
                                                                });
    }

    public void deleteProgramDiscipline(ProgramDisciplineId id){
        final ProgramDiscipline programDiscipline = programDisciplineRepository.findByProgramId(id);
        programDisciplineRepository.delete(programDiscipline);
    }
}
