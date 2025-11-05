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
    Course toEntity(final CourseDto courseDto);

    @Mapping(source = "major.id", target = "majorId")
    CourseDto toDto(final Course course);

    @Mapping(source = "majorId", target = "major.id")
    void updateEntityFromDto(final CourseDto courseDto, @MappingTarget final Course course);

    @Mapping(target = "id", ignore = true)
    CourseDto toInnerDto(final CourseRequestDto courseRequestDto);

    CourseResponseDto toResponseDto(final CourseDto courseDto);

    List<CourseResponseDto> toResponseDtoList(final List<CourseDto> courses);
}
