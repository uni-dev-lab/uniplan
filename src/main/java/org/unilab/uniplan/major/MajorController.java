package org.unilab.uniplan.major;

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

@RestController
@RequestMapping("/majors")
@RequiredArgsConstructor
public class MajorController {

    private static final String MAJOR_NOT_FOUND = "Major with ID {0} not found.";

    private final MajorService majorService;
    private final MajorMapper majorMapper;

    @PostMapping
    public ResponseEntity<MajorResponseDTO> addMajor(@RequestBody @NotNull
                                                     @Valid final MajorRequestDTO majorRequestDTO) {
        final MajorDTO majorDTO = majorMapper.toInnerDTO(majorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(majorMapper.toResponseDTO(majorService.createMajor(majorDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorResponseDTO> getMajorById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(majorMapper.toResponseDTO(majorService.findMajorById(id)
                                                                       .orElseThrow(() -> new ResponseStatusException(
                                                                           HttpStatus.NOT_FOUND,
                                                                           MessageFormat.format(
                                                                               MAJOR_NOT_FOUND,
                                                                               id)))));
    }

    @GetMapping
    public List<MajorResponseDTO> getAllMajors() {
        return majorMapper.toResponseDTOList(majorService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MajorResponseDTO> updateMajor(@PathVariable @NotNull final UUID id,
                                                        @RequestBody @NotNull @Valid MajorRequestDTO majorRequestDTO) {
        final MajorDTO majorDTO = majorMapper.toInnerDTO(majorRequestDTO);
        return ResponseEntity.ok(majorMapper.toResponseDTO(majorService.updateMajor(id, majorDTO)
                                                                       .orElseThrow(() -> new ResponseStatusException(
                                                                           HttpStatus.NOT_FOUND,
                                                                           MessageFormat.format(
                                                                               MAJOR_NOT_FOUND,
                                                                               id)))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable @NotNull final UUID id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}
