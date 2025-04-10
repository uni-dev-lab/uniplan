package org.unilab.uniplan.category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setRoomType(categoryDto.roomType());
        category.setCapacity(categoryDto.capacity());
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                         .map(categoryMapper::toDto)
                         .toList();
    }

    public Optional<CategoryDto> getCategoryById(UUID id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::toDto);
    }

    @Transactional
    public Optional<CategoryDto> updateCategory(UUID id, CategoryDto categoryDto) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setRoomType(categoryDto.roomType());
            category.setCapacity(categoryDto.capacity());
            category = categoryRepository.save(category);
            return Optional.of(categoryMapper.toDto(category));
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
