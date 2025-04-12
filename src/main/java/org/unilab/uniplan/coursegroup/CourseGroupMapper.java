package org.unilab.uniplan.coursegroup;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseGroupMapper {
    CourseGroupDTO toDTO (CourseGroup courseGroup);
    CourseGroup toEntity (CourseGroupDTO courseGroupDTO);
}
