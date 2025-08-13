package org.unilab.uniplan.programdisciplinelector;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineLectorService {

    private static final String PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND = "Program discipline lector with lectorId {0}, programId {1} and disciplineId {2} not found.";

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper;

    private final ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    @Transactional
    public ProgramDisciplineLectorDto createProgramDisciplineLector(ProgramDisciplineLectorDto programDisciplineLectorDto) {
        final ProgramDisciplineLector programDisciplineLector = programDisciplineLectorMapper.toEntity(
            programDisciplineLectorDto);

        return saveEntityAndConvertToDto(programDisciplineLector);
    }

    public List<ProgramDisciplineLectorDto> getAllProgramDisciplineLectors() {
        final List<ProgramDisciplineLector> programDisciplineLectors = programDisciplineLectorRepository.findAll();
        return programDisciplineLectorMapper.toDtos(programDisciplineLectors);
    }

    public Optional<ProgramDisciplineLectorDto> getProgramDisciplineLectorById(
        final UUID lectorId, final UUID programId, final UUID disciplineId) {
        final ProgramDisciplineLectorId id = programDisciplineLectorMapper.toProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        return programDisciplineLectorRepository.findById(id)
                                                .map(programDisciplineLectorMapper::toDto);
    }

    @Transactional
    public Optional<ProgramDisciplineLectorDto> updateProgramDisciplineLector(
        final UUID lectorId, final UUID programId, final UUID disciplineId,
        ProgramDisciplineLectorDto programDisciplineLectorDto) {
        final ProgramDisciplineLectorId id = programDisciplineLectorMapper.toProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        return programDisciplineLectorRepository.findById(id)
                                                .map(existingProgramDisciplineLector -> updateAndSaveEntityAndConvertToDto(
                                                    programDisciplineLectorDto,
                                                    existingProgramDisciplineLector));
    }

    public void deleteProgramDisciplineLector(final UUID lectorId,
                                              final UUID programId,
                                              final UUID disciplineId) {
        final ProgramDisciplineLectorId id = programDisciplineLectorMapper.toProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        final ProgramDisciplineLector programDisciplineLector = programDisciplineLectorRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException(
                MessageFormat.format(
                    PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND,
                    lectorId,
                    programId,
                    disciplineId
                )
            ));
        programDisciplineLectorRepository.delete(programDisciplineLector);
    }

    private ProgramDisciplineLectorDto updateAndSaveEntityAndConvertToDto(final ProgramDisciplineLectorDto dto,
                                                                          final ProgramDisciplineLector entity) {
        programDisciplineLectorMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private ProgramDisciplineLectorDto saveEntityAndConvertToDto(final ProgramDisciplineLector entity) {
        final ProgramDisciplineLector savedEntity = programDisciplineLectorRepository.save(entity);
        return programDisciplineLectorMapper.toDto(savedEntity);
    }
}
