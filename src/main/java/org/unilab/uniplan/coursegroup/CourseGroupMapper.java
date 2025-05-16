package org.unilab.uniplan.coursegroup;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface CourseGroupMapper {

    @Mapping(source = "course.id", target = "courseId")
    CourseGroupDTO toDTO(CourseGroup courseGroup);

    @Mapping(source = "courseId", target = "course.id")
    CourseGroup toEntity(CourseGroupDTO courseGroupDTO);

    @Mapping(source = "courseId", target = "course.id")
    void updateEntityFromDTO(CourseGroupDTO courseGroupDTO, @MappingTarget CourseGroup courseGroup);

    @Mapping(target = "id", ignore = true)
    CourseGroupDTO toInnerDTO(CourseGroupRequestDTO courseGroupRequestDTO);

    @Mapping(source = "courseId", target = "courseId")
    CourseGroupResponseDTO toResponseDTO(CourseGroupDTO courseGroupDTO);
    
    List<CourseGroupResponseDTO> toResponseDTOList(List<CourseGroupDTO> courseGroupDTOs);
}
