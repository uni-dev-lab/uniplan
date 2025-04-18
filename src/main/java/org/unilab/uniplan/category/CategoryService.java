package org.unilab.uniplan.category;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;

@Service
@RequiredArgsConstructor
public class CategoryService {

    public static final String CATEGORY_NOT_FOUND = "Category with ID {0} not found.";
    public static final String INVALID_CATEGORY_DATA = "Invalid category data provided.";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public Optional<CategoryResponseDto> createCategory(final CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto == null
            || categoryRequestDto.roomType() == null
            || categoryRequestDto.capacity() <= 0) {
            throw new RuntimeException(INVALID_CATEGORY_DATA);
        }

        final Category category = categoryMapper.toEntity(categoryRequestDto);
        final Category savedCategory = categoryRepository.save(category);

        return Optional.of(categoryMapper.toDto(savedCategory));
    }

    public List<CategoryResponseDto> getAllCategories() {
        final List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    public Optional<CategoryResponseDto> getCategoryById(final UUID id) {
        final Category category = categoryRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException(
                                                        MessageFormat.format(
                                                            CATEGORY_NOT_FOUND,
                                                            id)));

        return Optional.of(categoryMapper.toDto(category));
    }

    @Transactional
    public Optional<CategoryResponseDto> updateCategory(final UUID id,
                                                        final CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto == null
            || categoryRequestDto.roomType() == null
            || categoryRequestDto.capacity() <= 0) {
            throw new RuntimeException(INVALID_CATEGORY_DATA);
        }

        final Category existingCategory = categoryRepository.findById(id)
                                                            .orElseThrow(() -> new RuntimeException(
                                                                MessageFormat.format(
                                                                    CATEGORY_NOT_FOUND,
                                                                    id)));
        categoryMapper.updateEntityFromDto(categoryRequestDto, existingCategory);
        final Category savedCategory = categoryRepository.save(existingCategory);

        return Optional.of(categoryMapper.toDto(savedCategory));
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
