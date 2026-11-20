package org.unilab.uniplan.lector;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.unilab.uniplan.common.dto.PageResponseDto;
import org.unilab.uniplan.lector.dto.LectorFilterDto;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@RestController
@RequestMapping("/lectors")
@RequiredArgsConstructor
@Tag(name = "Lectors", description =
    "Manage lecturers with contact information and associated faculties")
public class LectorController {

    private final LectorWebFacade lectorWebFacade;

    @PostMapping
    public ResponseEntity<LectorResponseDto> createLector(
        @Valid @NotNull @RequestBody final LectorRequestDto lectorRequestDto) {
        final LectorResponseDto response = lectorWebFacade.createLector(lectorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<LectorResponseDto>> getAllLectors(
        @ParameterObject @PageableDefault(
            sort = {"firstName", "id"},
            direction = Direction.ASC) final Pageable pageable,
        @Valid @ParameterObject final LectorFilterDto filter) {
        return ResponseEntity.ok(lectorWebFacade.getAllLectors(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectorResponseDto> getLectorById(@PathVariable final UUID id) {
        return ResponseEntity.ok(lectorWebFacade.getLectorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectorResponseDto> updateLector(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final LectorRequestDto lectorRequestDto) {

        return ResponseEntity.ok(lectorWebFacade.updateLector(id, lectorRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLector(@PathVariable final UUID id) {
        lectorWebFacade.deleteLectorById(id);
        return ResponseEntity.noContent().build();
    }
}
