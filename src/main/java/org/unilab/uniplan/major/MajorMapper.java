package org.unilab.uniplan.major;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.major.dto.MajorCourseDto;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@Mapper
public interface MajorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Major toEntity(MajorDto majorDTO);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDto toDTO(Major major);

    @Mapping(source = "majorDto.facultyId", target = "faculty")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(MajorDto majorDto, @MappingTarget Major major);

    default Faculty mapFacultyId(UUID facultyId) {
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        return faculty;
    }

    @Mapping(target = "id", ignore = true)
    MajorDto toInnerDTO(MajorRequestDto requestDTO);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorResponseDto toResponseDTO(MajorDto innerDTO);

    List<MajorResponseDto> toResponseDTOList(List<MajorDto> majors);

    MajorCourseDto toDTO(MajorCourseModel majorCourseModel);
}