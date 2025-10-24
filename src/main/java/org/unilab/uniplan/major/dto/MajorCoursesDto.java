package org.unilab.uniplan.major.dto;

import java.util.List;
import java.util.UUID;
import org.unilab.uniplan.course.dto.CourseDto;

public record MajorCoursesDto(UUID id,
                              UUID facultyId,
                              String majorName,
                              List<CourseDto> courses) {

}
