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
    CourseGroupDto toDTO(CourseGroup courseGroup);

    @Mapping(source = "courseId", target = "course.id")
    CourseGroup toEntity(CourseGroupDto courseGroupDTO);

    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDTO(CourseGroupDto courseGroupDTO, @MappingTarget CourseGroup courseGroup);

    @Mapping(target = "id", ignore = true)
    CourseGroupDto toInnerDTO(CourseGroupRequestDto courseGroupRequestDTO);

    @Mapping(source = "courseId", target = "courseId")
    CourseGroupResponseDto toResponseDTO(CourseGroupDto courseGroupDTO);

    List<CourseGroupResponseDto> toResponseDTOList(List<CourseGroupDto> courseGroupDtos);
}
