package org.unilab.uniplan.lector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;


@Service
@RequiredArgsConstructor
public class LectorService implements BaseService<Lector> {

    private final LectorRepository lectorRepository;


    @Override
    public void save(final Lector entity) {
        lectorRepository.save(entity);
    }

    @Override
    public List<Lector> getAll() {
        return lectorRepository.findAll();
    }

    @Override
    public Optional<Lector> getById(final UUID id) {
        return lectorRepository.findById(id);
    }

    @Override
    public void delete(final Lector entity) {
        lectorRepository.delete(entity);
    }
}

