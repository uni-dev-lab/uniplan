package org.unilab.uniplan.discipline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class DisciplineService implements BaseService<Discipline> {

    private final DisciplineRepository disciplineRepository;

    @Override
    public void save(final Discipline discipline) {
        disciplineRepository.save(discipline);
    }

    @Override
    public Optional<Discipline> getById(final UUID id) {
        return disciplineRepository.findById(id);
    }

    @Override
    public List<Discipline> getAll() {
        return disciplineRepository.findAll();
    }

    @Override
    public void delete(final Discipline discipline) {
        disciplineRepository.delete(discipline);
    }
}
