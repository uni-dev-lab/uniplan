package org.unilab.uniplan.lector;

import static org.springframework.http.ResponseEntity.ok;

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
import org.unilab.uniplan.lector.dto.LectorDto;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@RestController
@RequestMapping("/lectors")
@RequiredArgsConstructor
@Tag(name = "Lectors", description = "Manage lecturers with contact information and associated faculties")
public class LectorController {

    private final LectorService lectorService;
    private final LectorMapper lectorMapper;

    @PostMapping
    public ResponseEntity<LectorResponseDto> createLector(@Valid @NotNull @RequestBody final LectorRequestDto lectorRequestDto) {
        final LectorDto lectorDto = lectorService.createLector(lectorMapper.toInternalDto(lectorRequestDto));

        return new ResponseEntity<>(lectorMapper.toResponseDto(lectorDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<LectorResponseDto> getAllLectors() {
        return lectorMapper.toResponseDtoList(lectorService.getAllLectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectorResponseDto> getLectorById(@NotNull @PathVariable UUID id) {
        final LectorDto lectorDto = lectorService.getLectorById(id);

        return ok(lectorMapper.toResponseDto(lectorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectorResponseDto> updateLector(@NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody LectorRequestDto lectorRequestDto) {
        final LectorDto lectorDto =lectorMapper.toInternalDto(lectorRequestDto);

        return ok(lectorMapper.toResponseDto(lectorService.updateLector(id, lectorDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLector(@NotNull @PathVariable UUID id) {
        lectorService.deleteLector(id);

        return ResponseEntity.noContent().build();
    }
}
