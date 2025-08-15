package org.unilab.uniplan.programdisciplinelector;

import static org.unilab.uniplan.utils.ErrorConstants.PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineLectorService {

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

    public ProgramDisciplineLectorDto getProgramDisciplineLectorById(
        final UUID lectorId, final UUID programId, final UUID disciplineId) {
        final ProgramDisciplineLectorId id = programDisciplineLectorMapper.toProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        return programDisciplineLectorRepository.findById(id)
                                                .map(programDisciplineLectorMapper::toDto)
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                    PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND.getMessage(
                                                        id.toString())));
    }

    @Transactional
    public ProgramDisciplineLectorDto updateProgramDisciplineLector(
        final UUID lectorId, final UUID programId, final UUID disciplineId,
        ProgramDisciplineLectorDto programDisciplineLectorDto) {
        final ProgramDisciplineLectorId id = programDisciplineLectorMapper.toProgramDisciplineLectorId(
            lectorId,
            programId,
            disciplineId);
        return programDisciplineLectorRepository.findById(id)
                                                .map(existingProgramDisciplineLector -> updateEntityAndConvertToDto(
                                                    programDisciplineLectorDto,
                                                    existingProgramDisciplineLector))
                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                    PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND.getMessage(
                                                        id.toString())));
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
            .orElseThrow(() -> new ResourceNotFoundException(
                PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND.getMessage(id.toString())
            ));
        programDisciplineLectorRepository.delete(programDisciplineLector);
    }

    private ProgramDisciplineLectorDto updateEntityAndConvertToDto(final ProgramDisciplineLectorDto dto,
                                                                   final ProgramDisciplineLector entity) {
        programDisciplineLectorMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private ProgramDisciplineLectorDto saveEntityAndConvertToDto(final ProgramDisciplineLector entity) {
        final ProgramDisciplineLector savedEntity = programDisciplineLectorRepository.save(entity);
        return programDisciplineLectorMapper.toDto(savedEntity);
    }
}
