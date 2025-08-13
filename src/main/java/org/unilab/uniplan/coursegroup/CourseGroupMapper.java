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
    CourseGroupDto toDto(CourseGroup courseGroup);

    @Mapping(source = "courseId", target = "course.id")
    CourseGroup toEntity(CourseGroupDto courseGroupDto);

    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDto(CourseGroupDto courseGroupDto, @MappingTarget CourseGroup courseGroup);

    @Mapping(target = "id", ignore = true)
    CourseGroupDto toInnerDto(CourseGroupRequestDto courseGroupRequestDto);

    @Mapping(source = "courseId", target = "courseId")
    CourseGroupResponseDto toResponseDto(CourseGroupDto courseGroupDto);

    List<CourseGroupResponseDto> toResponseDtoList(List<CourseGroupDto> courseGroupDtos);
}
