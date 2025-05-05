package org.unilab.uniplan.course;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper
public interface CourseMapper {
    @Mapping(source = "majorId", target = "major.id")
    Course toEntity (CourseDTO courseDTO);
    @Mapping(source = "major.id", target = "majorId")
    CourseDTO toDTO (Course course);
    
    @Mapping(source = "majorId" , target = "major.id")
    void updateEntityFromDTO(CourseDTO courseDTO, @MappingTarget Course course);

    @Mapping(target = "id", ignore = true)
    CourseDTO toInnerDTO(CourseRequestDTO courseRequestDTO);
    @Mapping(source = "majorId", target = "majorId")
    CourseResponseDTO toResponseDTO(CourseDTO courseDTO);
    List<CourseResponseDTO> toResponseDTOList(List<CourseDTO> courses);
}
