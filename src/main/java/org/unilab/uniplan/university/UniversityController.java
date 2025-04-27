package org.unilab.uniplan.university;

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
import org.unilab.uniplan.university.dto.UniversityDto;
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    public static final String UNIVERSITY_NOT_FOUND = "University with ID {0} not found.";

    private final UniversityService universityService;
    private final UniversityMapper universityMapper;

    @PostMapping
    public ResponseEntity<UniversityResponseDto> createUniversity(
        @Valid @NotNull @RequestBody final UniversityRequestDto universityRequestDto) {

        UniversityDto universityDto = universityService.createUniversity(
            universityMapper.toInternalDto(universityRequestDto));

        return new ResponseEntity<>(
            universityMapper.toResponseDto(universityDto),
            HttpStatus.CREATED
        );
    }

    @GetMapping
    public List<UniversityResponseDto> getAllUniversities() {
        return universityMapper.toResponseDtoList(universityService.getAllUniversities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponseDto> getUniversityById(@NotNull @PathVariable final UUID id) {
        var universityDto = universityService.getUniversityById(id)
                                             .orElseThrow(() -> new ResponseStatusException(
                                                 HttpStatus.NOT_FOUND,
                                                 MessageFormat.format(UNIVERSITY_NOT_FOUND, id)
                                             ));

        return ResponseEntity.ok(universityMapper.toResponseDto(universityDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityResponseDto> updateUniversity(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final UniversityRequestDto universityRequestDto) {

        UniversityDto universityDto = universityMapper.toInternalDto(universityRequestDto);

        return universityService.updateUniversity(id, universityDto)
                                .map(universityMapper::toResponseDto)
                                .map(ResponseEntity::ok)
                                .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    MessageFormat.format(UNIVERSITY_NOT_FOUND, id)
                                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable final UUID id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
