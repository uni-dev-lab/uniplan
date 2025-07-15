package org.unilab.uniplan.course;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;

@Mapper
public interface CourseMapper {

    @Mapping(source = "majorId", target = "major.id")
    Course toEntity(CourseDto courseDTO);

    @Mapping(source = "major.id", target = "majorId")
    CourseDto toDTO(Course course);

    @Mapping(source = "majorId", target = "major.id")
    void updateEntityFromDTO(CourseDto courseDTO, @MappingTarget Course course);

    @Mapping(target = "id", ignore = true)
    CourseDto toInnerDTO(CourseRequestDto courseRequestDTO);

    @Mapping(source = "majorId", target = "majorId")
    CourseResponseDto toResponseDTO(CourseDto courseDTO);

    List<CourseResponseDto> toResponseDTOList(List<CourseDto> courses);
}
