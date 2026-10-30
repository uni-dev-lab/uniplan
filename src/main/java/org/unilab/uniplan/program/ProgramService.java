package org.unilab.uniplan.program;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;
@Service
@RequiredArgsConstructor
public class ProgramService implements BaseService<Program> {

    private final ProgramRepository programRepository;

    @Override
    public Program save(final Program entity) {
        return programRepository.save(entity);
    }

    @Override
    public List<Program> getAll() {
        return programRepository.findAll();
    }

    @Override
    public Optional<Program> getById(final UUID id) {
        return programRepository.findById(id);
    }

    @Override
    public void delete(final Program entity) {
        programRepository.delete(entity);
    }
}
