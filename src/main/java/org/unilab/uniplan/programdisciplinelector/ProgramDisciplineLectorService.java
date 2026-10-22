package org.unilab.uniplan.programdisciplinelector;


import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramDisciplineLectorService {

    private final ProgramDisciplineLectorRepository programDisciplineLectorRepository;

    public ProgramDisciplineLector save(final ProgramDisciplineLector programDisciplineLector) {
        return programDisciplineLectorRepository.save(programDisciplineLector);
    }

    public List<ProgramDisciplineLector> getAll() {
        return programDisciplineLectorRepository.findAll();
    }

    public Optional<ProgramDisciplineLector> getById(final ProgramDisciplineLectorId id) {
        return programDisciplineLectorRepository.findById(id);
    }

    public void delete(final ProgramDisciplineLector programDisciplineLector) {
        programDisciplineLectorRepository.delete(programDisciplineLector);
    }
}
