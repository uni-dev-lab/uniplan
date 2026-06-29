package org.unilab.uniplan.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.CATEGORY_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoryWebFacade {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public void createCategory(final CategoryRequestDto request) {
        final Category category = categoryMapper.toEntity(request);
        categoryService.save(category);
    }

    @Transactional
    public void updateCategory(final UUID id,
                                                  final CategoryRequestDto request) {
        final Category category = getCategoryOrThrow(id);

        categoryMapper.updateEntityFromDto(request, category);
        categoryService.save(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(final UUID id) {
        final Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponseDto(category);
    }

    @Transactional
    public void deleteCategory(final UUID id) {
        final Category category = getCategoryOrThrow(id);
        categoryService.delete(category);
    }

    @Transactional
    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.toResponseDtoList(categoryService.getAll());
    }
    private Category getCategoryOrThrow(final UUID id) {
        return categoryService.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                CATEGORY_NOT_FOUND.getMessage(String.valueOf(id)))
            );
    }
}
