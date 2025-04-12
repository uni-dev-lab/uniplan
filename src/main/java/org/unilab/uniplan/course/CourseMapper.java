package org.unilab.uniplan.course;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity (CourseDTO courseDTO);
    CourseDTO toDTO (Course course);
}
