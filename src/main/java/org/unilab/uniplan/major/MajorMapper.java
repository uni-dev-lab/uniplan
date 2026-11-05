package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.course.CourseMapper;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@Mapper(uses = {CourseMapper.class})
public interface MajorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Major toEntity(MajorRequestDto majorRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "facultyId", target = "faculty")
    void updateEntityFromDto(MajorRequestDto majorRequestDto, @MappingTarget Major major);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorResponseDto toResponseDto(Major major);

    List<MajorResponseDto> toResponseDtoList(List<Major> majors);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorCoursesResponseDto toFullResponseDto(Major major);

    List<MajorCoursesResponseDto> toFullResponseDtoList(List<Major> majors);

    @Mapping(target = "id", source = "facultyId")
    Faculty toFaculty(UUID facultyId);
}