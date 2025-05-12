package org.unilab.uniplan.category;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.category.dto.CategoryDto;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testCreateCategoryShouldSaveAndReturnDto() {
        UUID id = UUID.randomUUID();
        String roomType = "Lecture Hall";
        short roomCapacity = 50;

        CategoryDto dto = new CategoryDto(id, roomType, roomCapacity);
        Category entity = new Category();
        Category saved = new Category();
        CategoryDto savedDto = new CategoryDto(id, roomType, roomCapacity);

        when(categoryMapper.toEntity(dto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(savedDto);

        CategoryDto result = categoryService.createCategory(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testAllCategoriesShouldReturnListOfCategoryDtos() {
        List<Category> entities = List.of(new Category());
        List<CategoryDto> dtos = List.of(new CategoryDto(UUID.randomUUID(), "Lab", (short) 30));

        when(categoryRepository.findAll()).thenReturn(entities);
        when(categoryMapper.toDtoList(entities)).thenReturn(dtos);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(dtos, result);
    }

    @Test
    void testFindCategoryByIdShouldReturnCategoryDtoIfFound() {
        UUID id = UUID.randomUUID();
        Category entity = new Category();
        CategoryDto dto = new CategoryDto(id, "Seminar Room", (short) 20);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        Optional<CategoryDto> result = categoryService.getCategoryById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testFindCategoryByIdShouldReturnEmptyOptionalIfCategoryNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CategoryDto> result = categoryService.getCategoryById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateCategoryShouldUpdateAndReturnDtoIfFound() {
        UUID id = UUID.randomUUID();
        String roomType = "Updated Room";
        short roomCapacity = 30;

        CategoryDto dto = new CategoryDto(id, roomType, roomCapacity);
        Category existing = new Category();
        Category updated = new Category();
        CategoryDto updatedDto = new CategoryDto(id, roomType, roomCapacity);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> null).when(categoryMapper).updateEntityFromDto(dto, existing);
        when(categoryRepository.save(existing)).thenReturn(updated);
        when(categoryMapper.toDto(updated)).thenReturn(updatedDto);

        Optional<CategoryDto> result = categoryService.updateCategory(id, dto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }

    @Test
    void testUpdateCategoryShouldReturnEmptyOptionalIfNotFound() {
        UUID id = UUID.randomUUID();
        CategoryDto dto = new CategoryDto(id, "Room", (short) 10);

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CategoryDto> result = categoryService.updateCategory(id, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteCategoryShouldDeleteCategoryIfFound() {
        UUID id = UUID.randomUUID();
        Category entity = new Category();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(categoryRepository).delete(entity);

        assertDoesNotThrow(() -> categoryService.deleteCategory(id));
        verify(categoryRepository).delete(entity);
    }

    @Test
    void testDeleteCategoryShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            categoryService.deleteCategory(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}