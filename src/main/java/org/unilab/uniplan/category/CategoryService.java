package org.unilab.uniplan.category;

import static org.unilab.uniplan.utils.ErrorConstants.CATEGORY_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.category.dto.CategoryDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto createCategory(final CategoryDto categoryDto) {
        final Category category = categoryMapper.toEntity(categoryDto);

        return saveEntityAndConvertToDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        final List<Category> categories = categoryRepository.findAll();

        return categoryMapper.toDtoList(categories);
    }

    public CategoryDto getCategoryById(final UUID id) {
        return categoryRepository.findById(id)
                                 .map(categoryMapper::toDto)
                                 .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND.getMessage(
                                     id.toString())));
    }

    @Transactional
    public CategoryDto updateCategory(final UUID id, final CategoryDto categoryDto) {
        return categoryRepository.findById(id)
                                 .map(existingCategory -> updateEntityAndConvertToDto(
                                     categoryDto,
                                     existingCategory))
                                 .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND.getMessage(
                                     id.toString())));
    }


    @Transactional
    public void deleteCategory(final UUID id) {
        final Category category = categoryRepository.findById(id)
                                                    .orElseThrow(() -> new ResourceNotFoundException(
                                                        CATEGORY_NOT_FOUND.getMessage(id.toString())));
        categoryRepository.delete(category);
    }


    private CategoryDto updateEntityAndConvertToDto(final CategoryDto dto,
                                                    final Category entity) {
        categoryMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private CategoryDto saveEntityAndConvertToDto(final Category entity) {
        final Category savedEntity = categoryRepository.save(entity);
        return categoryMapper.toDto(savedEntity);
    }
}
