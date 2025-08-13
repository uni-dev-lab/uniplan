package org.unilab.uniplan.programdiscipline;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineService {

    private static final String PROGRAM_DISCIPLINE_NOT_FOUND = "Program discipline with disciplineId {0} and programId {1} not found.";

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

    public Optional<ProgramDisciplineDto> getProgramDisciplineById(final UUID disciplineId,
                                                                   final UUID programId) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        return programDisciplineRepository.findById(id)
                                          .map(programDisciplineMapper::toDto);
    }

    @Transactional
    public Optional<ProgramDisciplineDto> updateProgramDiscipline(final UUID disciplineId,
                                                                  final UUID programId,
                                                                  ProgramDisciplineDto programDisciplineDto) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        return programDisciplineRepository.findById(id)
                                          .map(existingProgramDiscipline -> updateAndSaveEntityAndConvertToDto(
                                              programDisciplineDto,
                                              existingProgramDiscipline));
    }

    @Transactional
    public void deleteProgramDiscipline(final UUID disciplineId, final UUID programId) {
        final ProgramDisciplineId id = programDisciplineMapper.toProgramDisciplineId(disciplineId,
                                                                                     programId);
        final ProgramDiscipline programDiscipline = programDisciplineRepository.findById(id)
                                                                               .orElseThrow(() -> new RuntimeException(
                                                                                   MessageFormat.format(
                                                                                       PROGRAM_DISCIPLINE_NOT_FOUND,
                                                                                       disciplineId,
                                                                                       programId)));
        programDisciplineRepository.delete(programDiscipline);
    }


    private ProgramDisciplineDto updateAndSaveEntityAndConvertToDto(final ProgramDisciplineDto dto,
                                                                    final ProgramDiscipline entity) {
        programDisciplineMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private ProgramDisciplineDto saveEntityAndConvertToDto(final ProgramDiscipline entity) {
        final ProgramDiscipline savedEntity = programDisciplineRepository.save(entity);
        return programDisciplineMapper.toDto(savedEntity);
    }
}
