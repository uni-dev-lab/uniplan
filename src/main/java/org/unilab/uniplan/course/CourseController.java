package org.unilab.uniplan.course;

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
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @PostMapping
    public ResponseEntity<CourseResponseDto> addCourse(@RequestBody @NotNull
                                                       @Valid final CourseRequestDto courseRequestDTO) {
        final CourseDto courseDTO = courseMapper.toInnerDto(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(courseMapper.toResponseDto(courseService.createCourse(courseDTO)));
    }

    @GetMapping("/with-Major/{majorId}")
    public  List<CourseResponseDto> getCoursesByMajorId(@PathVariable @NotNull final UUID majorId) {
        return courseMapper.toResponseDtoList(courseService.findAllByMajorId(majorId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getMajorById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(courseMapper.toResponseDto(courseService.findCourseById(id)));
    }

    @GetMapping
    public List<CourseResponseDto> getAllCourses() {
        return courseMapper.toResponseDtoList(courseService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable @NotNull final UUID id,
                                                          @RequestBody @NotNull @Valid final CourseRequestDto courseRequestDTO) {
        final CourseDto courseDTO = courseMapper.toInnerDto(courseRequestDTO);
        return ResponseEntity.ok(courseMapper.toResponseDto(courseService.updateCourse(id, courseDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable @NotNull final UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}