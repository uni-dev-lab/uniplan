package org.unilab.uniplan.lector;

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
import org.unilab.uniplan.lector.dto.LectorDto;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@RestController
@RequestMapping("/api/lectors")
@RequiredArgsConstructor
public class LectorController {

    public static final String LECTOR_NOT_FOUND = "Lector with ID {0} not found.";
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
        final LectorDto lectorDto = lectorService.getLectorById(id)
                                                       .orElseThrow(() -> new ResponseStatusException(
                                                           HttpStatus.NOT_FOUND,
                                                           MessageFormat.format(LECTOR_NOT_FOUND, id)
                                                       ));
        return ResponseEntity.ok(lectorMapper.toResponseDto(lectorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectorResponseDto> updateLector(@NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody LectorRequestDto lectorRequestDto) {
        final LectorDto lectorDto =lectorMapper.toInternalDto(lectorRequestDto);

        return lectorService.updateLector(id, lectorDto)
                            .map(lectorMapper::toResponseDto)
                            .map(ResponseEntity::ok)
                            .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                MessageFormat.format(LECTOR_NOT_FOUND,id)
                            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLector(@NotNull @PathVariable UUID id) {
        lectorService.deleteLector(id);

        return ResponseEntity.noContent().build();
    }
}
