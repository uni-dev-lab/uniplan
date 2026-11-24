package org.unilab.uniplan.lector;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;
import org.unilab.uniplan.common.model.FilterableService;
import org.unilab.uniplan.lector.dto.LectorFilterDto;

@Service
@RequiredArgsConstructor
public class LectorService implements BaseService<Lector>,
    FilterableService<Lector, LectorFilterDto> {

    private final LectorRepository lectorRepository;

    @Override
    public Lector save(final Lector entity) {
        return lectorRepository.save(entity);
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

    @Override
    public Page<Lector> getAll(final LectorFilterDto filter, final Pageable pageable) {
        final Specification<Lector> spec = LectorSpecifications.from(filter);
        return lectorRepository.findAll(spec, pageable);
    }
}

