package org.unilab.uniplan.discipline;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.discipline.dto.DisciplineDto;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private static final String DISCIPLINE_NOT_FOUND = "Discipline with ID {0} not found.";
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

    public Optional<DisciplineDto> getDisciplineById(@NotNull UUID id) {
        return disciplineRepository.findById(id)
                                   .map(disciplineMapper::toDto);
    }

    @Transactional
    public Optional<DisciplineDto> updateDiscipline(UUID id, @Valid DisciplineDto disciplineDto) {
        return disciplineRepository.findById(id)
                                   .map(existingDiscipline -> updateAndSaveEntityAndConvertToDto(
                                       disciplineDto,
                                       existingDiscipline));
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        final Discipline discipline =disciplineRepository.findById(id)
                                                         .orElseThrow(() -> new RuntimeException(
                                                             MessageFormat.format(
                                                                 DISCIPLINE_NOT_FOUND,
                                                                 id)));
        disciplineRepository.delete(discipline);
    }

    private DisciplineDto updateAndSaveEntityAndConvertToDto(final DisciplineDto dto,
                                                             final Discipline entity) {
        disciplineMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private DisciplineDto saveEntityAndConvertToDto(final Discipline entity) {
        final Discipline savedEntity = disciplineRepository.save(entity);
        return disciplineMapper.toDto(savedEntity);
    }
}
