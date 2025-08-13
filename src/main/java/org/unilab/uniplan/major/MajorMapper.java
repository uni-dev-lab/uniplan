package org.unilab.uniplan.major;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@Mapper
public interface MajorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Major toEntity(MajorDto majorDto);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDto toDto(Major major);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDto(MajorDto majorDto, @MappingTarget Major major);

    @Mapping(target = "id", ignore = true)
    MajorDto toInnerDto(MajorRequestDto requestDto);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorResponseDto toResponseDto(MajorDto innerDto);

    List<MajorResponseDto> toResponseDtoList(List<MajorDto> majors);
}