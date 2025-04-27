package org.unilab.uniplan.faculty;

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
import org.unilab.uniplan.faculty.dto.FacultyDto;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
public class FacultyController {

    public static final String FACULTY_NOT_FOUND = "Faculty with ID {0} not found.";

    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;

    @PostMapping
    public ResponseEntity<FacultyResponseDto> createFaculty(
        @Valid @NotNull @RequestBody final FacultyRequestDto facultyRequestDto) {

        final FacultyDto facultyDto = facultyService.createFaculty(facultyMapper.toInternalDto(
            facultyRequestDto));

        return new ResponseEntity<>(facultyMapper.toResponseDto(facultyDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public List<FacultyResponseDto> getAllFaculties() {
        return facultyMapper.toResponseDtoList(facultyService.getAllFaculties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> getFacultyById(@NotNull @PathVariable final UUID id) {
        final FacultyDto facultyDto = facultyService.getFacultyById(id)
                                                    .orElseThrow(() -> new ResponseStatusException(
                                                        HttpStatus.NOT_FOUND,
                                                        MessageFormat.format(FACULTY_NOT_FOUND, id)
                                                    ));
        return ResponseEntity.ok(facultyMapper.toResponseDto(facultyDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final FacultyRequestDto facultyRequestDto) {

        final FacultyDto internalDto = facultyMapper.toInternalDto(facultyRequestDto);

        return facultyService.updateFaculty(id, internalDto)
                             .map(facultyMapper::toResponseDto)
                             .map(ResponseEntity::ok)
                             .orElseThrow(() -> new ResponseStatusException(
                                 HttpStatus.NOT_FOUND,
                                 MessageFormat.format(FACULTY_NOT_FOUND, id)
                             ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable final UUID id) {
        facultyService.deleteFaculty(id);

        return ResponseEntity.noContent().build();
    }
}