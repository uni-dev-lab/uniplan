package org.unilab.uniplan.major.dto;

import java.util.List;
import java.util.UUID;
import org.unilab.uniplan.course.dto.CourseResponseDto;

public record MajorCoursesResponseDto(UUID id,
                                      UUID facultyId,
                                      String majorName,
                                      List<CourseResponseDto> courses) {

}
