package org.unilab.uniplan.programdiscipline;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ProgramDisciplineNotFoundException;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineService {

    private final ProgramDisciplineRepository programDisciplineRepository;

    private final ProgramDisciplineMapper programDisciplineMapper;

    @Transactional
    public ProgramDisciplineDto createProgramDiscipline(ProgramDisciplineDto programDisciplineDto){
        final ProgramDiscipline programDiscipline = programDisciplineMapper.toEntity(programDisciplineDto);

        return saveEntityAndConvertToDto(programDiscipline);
    }

    public List<ProgramDisciplineDto> getAllProgramDisciplines(){
        final List<ProgramDiscipline> programDisciplines = programDisciplineRepository.findAll();
        return programDisciplineMapper.toDtos(programDisciplines);
    }

    public ProgramDisciplineDto getProgramDisciplineById(final UUID disciplineId,
                                                         final UUID programId) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        return programDisciplineRepository.findById(id)
                                          .map(programDisciplineMapper::toDto)
                                          .orElseThrow(() -> new ProgramDisciplineNotFoundException(id));
    }

    @Transactional
    public ProgramDisciplineDto updateProgramDiscipline(final UUID disciplineId,
                                                        final UUID programId,
                                                        ProgramDisciplineDto programDisciplineDto) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        return programDisciplineRepository.findById(id)
                                          .map(existingProgramDiscipline -> updateEntityAndConvertToDto(
                                              programDisciplineDto,
                                              existingProgramDiscipline))
                                          .orElseThrow(() -> new ProgramDisciplineNotFoundException(id));
    }

    @Transactional
    public void deleteProgramDiscipline(final UUID disciplineId, final UUID programId) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        final ProgramDiscipline programDiscipline = programDisciplineRepository.findById(id)
                                                                               .orElseThrow(() -> new ProgramDisciplineNotFoundException(id));
        programDisciplineRepository.delete(programDiscipline);
    }


    private ProgramDisciplineDto updateEntityAndConvertToDto(final ProgramDisciplineDto dto,
                                                             final ProgramDiscipline entity) {
        programDisciplineMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private ProgramDisciplineDto saveEntityAndConvertToDto(final ProgramDiscipline entity) {
        final ProgramDiscipline savedEntity = programDisciplineRepository.save(entity);
        return programDisciplineMapper.toDto(savedEntity);
    }
}
