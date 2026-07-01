package org.unilab.uniplan.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.preinitialize.BackgroundPreinitializer;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class CategoryService implements BaseService<Category> {

    private final CategoryRepository categoryRepository;

    @Override
    public void save(final Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getById(final UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }
}
