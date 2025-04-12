package org.unilab.uniplan.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public Optional<CategoryDto> createCategory(final CategoryDto categoryDto) {
        final Category category = categoryMapper.toEntity(categoryDto);

        if (category == null || category.getRoomType() == null || category.getCapacity() <= 0) {
            return Optional.empty();
        }

        final Category savedCategory = categoryRepository.save(category);
        return Optional.of(categoryMapper.toDto(savedCategory));
    }

    public List<CategoryDto> getAllCategories() {
        final List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    public Optional<CategoryDto> getCategoryById(final UUID id) {
        final Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDto);
    }

    @Transactional
    public Optional<CategoryDto> updateCategory(final UUID id, final CategoryDto categoryDto) {
        final Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            final Category category = existingCategory.get();
            categoryMapper.updateEntityFromDto(categoryDto, category);
            final Category savedCategory = categoryRepository.save(category);
            return Optional.of(categoryMapper.toDto(savedCategory));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<CategoryDto> deleteCategory(final UUID id) {
        final Optional<Category> categoryOpt = categoryRepository.findById(id);

        if (categoryOpt.isPresent()) {
            final Category category = categoryOpt.get();
            categoryRepository.delete(category);
            return Optional.of(categoryMapper.toDto(category));
        }

        return Optional.empty();
    }
}
