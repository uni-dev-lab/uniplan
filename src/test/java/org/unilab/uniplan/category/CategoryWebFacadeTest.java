package org.unilab.uniplan.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryWebFacadeTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryWebFacade categoryWebFacade;

    private UUID id;
    private Category category;
    private CategoryRequestDto requestDto;
    private CategoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        category = new Category();
        requestDto = mock(CategoryRequestDto.class);
        responseDto = mock(CategoryResponseDto.class);
    }

    @Test
    void testCreateCategoryShouldSaveAndReturnCategory() {
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        final CategoryResponseDto result = categoryWebFacade.createCategory(requestDto);

        assertEquals(responseDto, result);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryMapper).toResponseDto(category);
        verify(categoryService).save(category);
    }

    @Test
    void testUpdateCategoryShouldSaveAndReturnResponseDto() {
        when(categoryService.getById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        final  CategoryResponseDto result = categoryWebFacade.updateCategory(id, requestDto);

        assertEquals(responseDto, result);
        verify(categoryMapper).toResponseDto(category);
        verify(categoryService).save(category);
        verify(categoryMapper).updateEntityFromDto(requestDto, category);
        verify(categoryService).getById(id);
    }

    @Test
    void testUpdateShouldThrowCategoryNotFound() {
        when(categoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> categoryWebFacade.updateCategory(id, requestDto));

        verify(categoryService).getById(id);
        verify(categoryService, never()).save(any(Category.class));
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void getCategoryByIdShouldReturnResponseDtoIfFound() {
        when(categoryService.getById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        final  CategoryResponseDto result = categoryWebFacade.getCategoryById(id);

        assertEquals(responseDto, result);
        verify(categoryMapper).toResponseDto(category);
        verify(categoryService).getById(id);
    }

    @Test
    void getCategoryByIdShouldThrowCategoryNotFound() {
        when(categoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> categoryWebFacade.getCategoryById(id));

        verify(categoryService).getById(id);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    void deleteCategoryShouldDeleteCategory() {
        when(categoryService.getById(id)).thenReturn(Optional.of(category));

        categoryWebFacade.deleteCategory(id);

        verify(categoryService).getById(id);
        verify(categoryService).delete(category);
    }

    @Test
    void deleteCategoryShouldThrowCategoryNotFound() {
        when(categoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> categoryWebFacade.deleteCategory(id));

        verify(categoryService).getById(id);
        verify(categoryService, never()).delete(any(Category.class));
    }

    @Test
    void getAllCategoriesShouldReturnResponseDtoList() {
        final List<Category> categories = List.of(category);
        final List<CategoryResponseDto> responses  = List.of(responseDto);

        when(categoryService.getAll()).thenReturn(categories);
        when(categoryMapper.toResponseDtoList(categories)).thenReturn(responses);

        final List<CategoryResponseDto> result = categoryWebFacade.getAllCategories();

        assertEquals(responses, result);
        verify(categoryMapper).toResponseDtoList(categories);
        verify(categoryService).getAll();
    }
}
