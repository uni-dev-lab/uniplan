package org.unilab.uniplan.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.category.dto.CategoryDto;
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    public static final String CATEGORY_NOT_FOUND = "Category with ID {0} not found.";

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {
        final CategoryDto categoryDto = categoryService.createCategory(categoryMapper.toInternalDto(
            categoryRequestDto));

        return new ResponseEntity<>(categoryMapper.toResponseDto(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryMapper.toResponseDtoList(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@NotNull @PathVariable final UUID id) {
        final CategoryDto categoryDto = categoryService.getCategoryById(id)
                                                 .orElseThrow(() -> new ResponseStatusException(
                                                     HttpStatus.NOT_FOUND,
                                                     MessageFormat.format(CATEGORY_NOT_FOUND, id)
                                                 ));

        return ResponseEntity.ok(categoryMapper.toResponseDto(categoryDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {

        final CategoryDto internalDto = categoryMapper.toInternalDto(categoryRequestDto);

        return categoryService.updateCategory(id, internalDto)
                              .map(categoryMapper::toResponseDto)
                              .map(ResponseEntity::ok)
                              .orElseThrow(() -> new ResponseStatusException(
                                  HttpStatus.NOT_FOUND,
                                  MessageFormat.format(CATEGORY_NOT_FOUND, id)
                              ));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final UUID id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
