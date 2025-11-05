package org.unilab.uniplan.coursegroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.coursegroup.dto.CourseGroupDto;
import org.unilab.uniplan.coursegroup.dto.CourseGroupRequestDto;
import org.unilab.uniplan.coursegroup.dto.CourseGroupResponseDto;

@Mapper
public interface CourseGroupMapper {

    @Mapping(source = "course.id", target = "courseId")
    CourseGroupDto toDto(final CourseGroup courseGroup);

    @Mapping(source = "courseId", target = "course.id")
    CourseGroup toEntity(final CourseGroupDto courseGroupDto);

    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDto(final CourseGroupDto courseGroupDto, @MappingTarget final CourseGroup courseGroup);

    @Mapping(target = "id", ignore = true)
    CourseGroupDto toInnerDto(final CourseGroupRequestDto courseGroupRequestDto);

    CourseGroupResponseDto toResponseDto(final CourseGroupDto courseGroupDto);

    List<CourseGroupResponseDto> toResponseDtoList(final List<CourseGroupDto> courseGroupDtos);
}
