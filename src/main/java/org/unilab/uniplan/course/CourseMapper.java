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
    Course toEntity(CourseDto courseDto);

    @Mapping(source = "major.id", target = "majorId")
    CourseDto toDto(Course course);

    @Mapping(source = "majorId", target = "major.id")
    void updateEntityFromDto(CourseDto courseDto, @MappingTarget Course course);

    @Mapping(target = "id", ignore = true)
    CourseDto toInnerDto(CourseRequestDto courseRequestDto);

    @Mapping(source = "majorId", target = "majorId")
    CourseResponseDto toResponseDto(CourseDto courseDto);

    List<CourseResponseDto> toResponseDtoList(List<CourseDto> courses);
}
