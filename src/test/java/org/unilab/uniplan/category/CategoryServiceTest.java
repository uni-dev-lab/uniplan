package org.unilab.uniplan.category;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.category.dto.CategoryDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private UUID id;
    private Category entity;
    private CategoryDto dto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        dto = new CategoryDto(id, "Lecture Hall", (short) 50);
        entity = new Category();
    }

    @Test
    void testCreateCategoryShouldSaveAndReturnDto() {
        when(categoryMapper.toEntity(dto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        CategoryDto result = categoryService.createCategory(dto);

        assertEquals(dto, result);
    }

    @Test
    void testAllCategoriesShouldReturnListOfCategoryDtos() {
        List<Category> entities = List.of(entity);
        List<CategoryDto> dtos = List.of(dto);

        when(categoryRepository.findAll()).thenReturn(entities);
        when(categoryMapper.toDtoList(entities)).thenReturn(dtos);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(dtos, result);
    }

    @Test
    void testFindCategoryByIdShouldReturnCategoryDtoIfFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        CategoryDto result = categoryService.getCategoryById(id);

        assertEquals(dto, result);
    }

    @Test
    void testFindCategoryByIdShouldReturnEmptyOptionalIfCategoryNotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testUpdateCategoryShouldUpdateAndReturnDtoIfFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(categoryMapper).updateEntityFromDto(dto, entity);
        when(categoryRepository.save(entity)).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        CategoryDto result = categoryService.updateCategory(id, dto);

        assertEquals(dto, result);
    }

    @Test
    void testUpdateCategoryShouldReturnEmptyOptionalIfNotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(id, dto));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testDeleteCategoryShouldDeleteCategoryIfFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(categoryRepository).delete(entity);

        assertDoesNotThrow(() -> categoryService.deleteCategory(id));
        verify(categoryRepository).delete(entity);
    }

    @Test
    void testDeleteCategoryShouldThrowIfNotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            categoryService.deleteCategory(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }
}
