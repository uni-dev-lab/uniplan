package org.unilab.uniplan.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private UUID id;
    private Category entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new Category();
    }

    @Test
    void testCreateCategoryShouldSaveCategory() {
        when(categoryRepository.save(entity)).thenReturn(entity);

        final Category result = categoryService.save(entity);
        assertEquals(entity, result);
        verify(categoryRepository).save(entity);
    }

    @Test
    void testAllCategoriesShouldReturnListOfCategory() {
        final List<Category> entities = List.of(entity);

        when(categoryRepository.findAll()).thenReturn(entities);

        final List<Category> result = categoryService.getAllCategories();

        assertEquals(entities, result);
        verify(categoryRepository).findAll();
    }

    @Test
    void testGetCategoryByIdShouldReturnCategoryOptional() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));

        final Optional<Category> result = categoryService.getCategoryById(id);

        assertEquals(Optional.of(entity), result);
        verify(categoryRepository).findById(id);
    }

    @Test
    void testGetCategoryByIdShouldReturnEmptyOptionalIfCategoryNotFound() {
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<Category> result = categoryService.getCategoryById(id);

        assertEquals(Optional.empty(), result);
        verify(categoryRepository).findById(id);
    }

    @Test
    void testDeleteCategoryShouldDeleteCategoryIfFound() {
        categoryService.deleteCategory(entity);

        verify(categoryRepository).delete(entity);
    }
}
