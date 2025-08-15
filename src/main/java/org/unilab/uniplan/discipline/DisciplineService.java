package org.unilab.uniplan.discipline;

import static org.unilab.uniplan.utils.ErrorConstants.DEPARTMENT_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.DISCIPLINE_NOT_FOUND;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.discipline.dto.DisciplineDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;


    @Transactional
    public DisciplineDto createDiscipline(@Valid DisciplineDto disciplineDto) {
        final Discipline discipline = disciplineMapper.toEntity(disciplineDto);

        return saveEntityAndConvertToDto(discipline);
    }

    public List<DisciplineDto> getAllDisciplines() {
        final List<Discipline> disciplines = disciplineRepository.findAll();

        return disciplineMapper.toDtoList(disciplines);
    }

    public DisciplineDto getDisciplineById(@NotNull UUID id) {
        return disciplineRepository.findById(id)
                                   .map(disciplineMapper::toDto)
                                   .orElseThrow(() -> new ResourceNotFoundException(
                                       DISCIPLINE_NOT_FOUND.getMessage(id.toString())));
    }

    @Transactional
    public DisciplineDto updateDiscipline(UUID id, @Valid DisciplineDto disciplineDto) {
        return disciplineRepository.findById(id)
                                   .map(existingDiscipline -> updateEntityAndConvertToDto(
                                       disciplineDto,
                                       existingDiscipline))
                                   .orElseThrow(() -> new ResourceNotFoundException(
                                       DISCIPLINE_NOT_FOUND.getMessage(id.toString())));
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        final Discipline discipline = disciplineRepository.findById(id)
                                                         .orElseThrow(() -> new ResourceNotFoundException(
                                                             DEPARTMENT_NOT_FOUND.getMessage(id.toString())));
        disciplineRepository.delete(discipline);
    }

    private DisciplineDto updateEntityAndConvertToDto(final DisciplineDto dto,
                                                      final Discipline entity) {
        disciplineMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private DisciplineDto saveEntityAndConvertToDto(final Discipline entity) {
        final Discipline savedEntity = disciplineRepository.save(entity);
        return disciplineMapper.toDto(savedEntity);
    }
}
