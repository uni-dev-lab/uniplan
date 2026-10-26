package org.unilab.uniplan.discipline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.DISCIPLINE_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class DisciplineWebFacade {

    private final DisciplineService disciplineService;
    private final DisciplineMapper disciplineMapper;

    @Transactional
    public DisciplineResponseDto createDiscipline(final DisciplineRequestDto request) {
        final Discipline discipline = disciplineMapper.toEntity(request);

        final Discipline saved = disciplineService.save(discipline);

        log.info("Created Discipline with ID: {}", saved.getId());

        return disciplineMapper.toResponseDto(saved);
    }

    @Transactional
    public DisciplineResponseDto updateDiscipline(final UUID id, final DisciplineRequestDto request) {
        final Discipline discipline = getDisciplineOrThrow(id);
        disciplineMapper.updateEntityFromDto(request, discipline);
        final Discipline saved = disciplineService.save(discipline);

        log.info("Updated Discipline with ID: {}", saved.getId());

        return disciplineMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DisciplineResponseDto> getAllDisciplines() {
        return disciplineMapper.toResponseDtoList(disciplineService.getAll());
    }

    @Transactional(readOnly = true)
    public DisciplineResponseDto getDisciplineById(final UUID id) {
        final Discipline discipline = getDisciplineOrThrow(id);

        return disciplineMapper.toResponseDto(discipline);
    }

    @Transactional
    public void deleteDiscipline(final UUID id) {
        final Discipline discipline = getDisciplineOrThrow(id);

        disciplineService.delete(discipline);

        log.info("Deleted Discipline with ID: {}", id);
    }

    private Discipline getDisciplineOrThrow(final UUID id) {
        return disciplineService.getById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    DISCIPLINE_NOT_FOUND.getMessage(String.valueOf(id))));
    }
}
