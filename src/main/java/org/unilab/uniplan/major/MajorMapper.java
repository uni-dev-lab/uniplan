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
    Major toEntity(final MajorDto majorDto);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDto toDto(final Major major);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDto(final MajorDto majorDto, @MappingTarget final Major major);

    @Mapping(target = "id", ignore = true)
    MajorDto toInnerDto(final MajorRequestDto requestDto);

    MajorResponseDto toResponseDto(final MajorDto innerDto);

    List<MajorResponseDto> toResponseDtoList(final List<MajorDto> majors);
}