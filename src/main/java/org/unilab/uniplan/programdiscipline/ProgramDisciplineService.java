package org.unilab.uniplan.programdiscipline;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineService {

    private final ProgramDisciplineRepository programDisciplineRepository;

    public ProgramDiscipline save(final ProgramDiscipline programDiscipline) {
        return programDisciplineRepository.save(programDiscipline);
    }

    public List<ProgramDiscipline> getAll() {
        return programDisciplineRepository.findAll();
    }

    public Optional<ProgramDiscipline> getById(final ProgramDisciplineId id) {
        return programDisciplineRepository.findById(id);
    }

    public void delete(final ProgramDiscipline programDiscipline) {
        programDisciplineRepository.delete(programDiscipline);
    }
}
