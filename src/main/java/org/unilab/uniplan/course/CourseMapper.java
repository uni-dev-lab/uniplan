package org.unilab.uniplan.course;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;

@Mapper
public interface CourseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "majorId", target = "major.id")
    Course toEntity(final CourseRequestDto courseRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "majorId", target = "major.id")
    void updateEntityFromDto(final CourseRequestDto courserequestDto, @MappingTarget final Course course);

    @Mapping(source = "major.id", target = "majorId")
    CourseResponseDto toResponseDto(final Course course);

    List<CourseResponseDto> toResponseDtoList(final List<Course> courses);
}
