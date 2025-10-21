package org.unilab.uniplan.major;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.course.CourseMapper;
import org.unilab.uniplan.major.dto.MajorCoursesDto;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@Mapper(uses = {CourseMapper.class})
public interface MajorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Major toEntity(MajorDto majorDto);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDto toDto(Major major);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorCoursesDto toFullDto(Major major);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDto(MajorDto majorDto, @MappingTarget Major major);

    @Mapping(target = "id", ignore = true)
    MajorDto toInnerDto(MajorRequestDto requestDto);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorResponseDto toResponseDto(MajorDto innerDto);

    List<MajorResponseDto> toResponseDtoList(List<MajorDto> majors);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorCoursesResponseDto toFullResponseDto(MajorCoursesDto innerDto);

    List<MajorCoursesResponseDto> toFullResponseDtoList(List<MajorCoursesDto> majors);
}