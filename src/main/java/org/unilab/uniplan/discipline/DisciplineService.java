package org.unilab.uniplan.discipline;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    private static final String DISCIPLINE_NOT_FOUND = "Discipline not found";
    public DisciplineService(DisciplineRepository disciplineRepository, DisciplineMapper disciplineMapper) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
    }

    @Transactional
    public DisciplineDto createDiscipline(@Valid DisciplineDto dto) {
        if (disciplineRepository.existsById(dto.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Discipline id must be unique");
        }

        Discipline discipline = disciplineMapper.toEntity(dto);
        Discipline saved = disciplineRepository.save(discipline);
        return disciplineMapper.toDto(saved);
    }

    public List<DisciplineDto> getAllDisciplines() {
        List<Discipline> disciplines = disciplineRepository.findAll();
        return disciplines.stream().map(disciplineMapper::toDto).toList();
    }

    public DisciplineDto getDisciplineById(UUID id) {
        Discipline discipline = disciplineRepository.findById(id)
                                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                                                   DISCIPLINE_NOT_FOUND));
        return disciplineMapper.toDto(discipline);
    }

    @Transactional
    public DisciplineDto updateDiscipline(UUID id, @Valid DisciplineDto dto) {
        Discipline existing = disciplineRepository.findById(id)
                                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                                                 DISCIPLINE_NOT_FOUND));

        if (!existing.getName().equals(dto.name()) && disciplineRepository.existsById(dto.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Discipline id must be unique");
        }

        existing.setName(dto.name());
        existing.setMainLector(dto.mainLector());
        existing.setProgramDisciplines(dto.programDisciplineList());

        Discipline updated = disciplineRepository.save(existing);
        return disciplineMapper.toDto(updated);
    }

    @Transactional
    public void deleteDiscipline(UUID id) {
        Discipline discipline = disciplineRepository.findById(id)
                                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                                                   DISCIPLINE_NOT_FOUND));

        try {
            disciplineRepository.delete(discipline);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Discipline is in use and cannot be deleted");
        }
    }
}
