package org.unilab.uniplan.lector;

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
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@RestController
@RequestMapping("/lectors")
@RequiredArgsConstructor
@Tag(name = "Lectors", description = "Manage lecturers with contact information and associated faculties")
public class LectorController {

    private final LectorWebFacade lectorWebFacade;

    @PostMapping
    public ResponseEntity<Void> createLector(@Valid @NotNull @RequestBody final LectorRequestDto lectorRequestDto) {
        lectorWebFacade.createLector(lectorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<LectorResponseDto>> getAllLectors() {
        return ResponseEntity.ok(lectorWebFacade.getAllLectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectorResponseDto> getLectorById(@NotNull @PathVariable final UUID id) {
       return ResponseEntity.ok(lectorWebFacade.getLectorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLector(@NotNull @PathVariable final UUID id, @Valid @NotNull @RequestBody final LectorRequestDto lectorRequestDto) {
        lectorWebFacade.updateLector(id, lectorRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLector(@NotNull @PathVariable final UUID id) {
        lectorWebFacade.deleteLectorById(id);
        return ResponseEntity.noContent().build();
    }
}
