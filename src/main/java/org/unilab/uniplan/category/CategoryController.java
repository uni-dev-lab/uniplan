package org.unilab.uniplan.category;

import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Room Categories", description = "Manage categories of rooms, including type and capacity")
public class CategoryController {

    private final CategoryWebFacade categoryWebFacade;

    @PostMapping
    public ResponseEntity<Void> createCategory(@Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {
        categoryWebFacade.createCategory(categoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok(categoryWebFacade.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable final UUID id) {
        return ResponseEntity.ok(categoryWebFacade.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final CategoryRequestDto categoryRequestDto) {
        categoryWebFacade.updateCategory(id, categoryRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable final UUID id) {
       categoryWebFacade.deleteCategory(id);
       return ResponseEntity.noContent().build();
    }
}
