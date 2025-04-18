package org.unilab.uniplan.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.unilab.uniplan.category.dto.CategoryRequestDto;
import org.unilab.uniplan.category.dto.CategoryResponseDto;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequestDto)
                                                   .orElseThrow(), HttpStatus.CREATED);
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@NotNull @PathVariable final UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {

        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequestDto)
                                                .orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final UUID id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
