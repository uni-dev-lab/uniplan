package org.unilab.uniplan.category;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.category.dto.CategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryService {

    public static final String CATEGORY_NOT_FOUND = "Category with ID {0} not found.";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto createCategory(final CategoryDto categoryDto) {
        final Category category = categoryMapper.toEntity(categoryDto);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    public List<CategoryDto> getAllCategories() {
        final List<Category> categories = categoryRepository.findAll();

        return categoryMapper.toDtoList(categories);
    }

    public Optional<CategoryDto> getCategoryById(final UUID id) {
        return categoryRepository.findById(id)
                                 .map(categoryMapper::toDto);
    }

    @Transactional
    public Optional<CategoryDto> updateCategory(final UUID id, final CategoryDto categoryDto) {
        return categoryRepository.findById(id)
                                 .map(existingCategory -> {
                                     categoryMapper.updateEntityFromDto(categoryDto,
                                                                        existingCategory);

                                     return categoryMapper.toDto(categoryRepository.save(
                                         existingCategory));
                                 });
    }


    @Transactional
    public void deleteCategory(final UUID id) {
        final Category category = categoryRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException(
                                                        MessageFormat.format(
                                                            CATEGORY_NOT_FOUND,
                                                            id)));
        categoryRepository.delete(category);
    }
}
