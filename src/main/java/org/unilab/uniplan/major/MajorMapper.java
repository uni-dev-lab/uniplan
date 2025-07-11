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
    Major toEntity(MajorDto majorDTO);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDto toDTO(Major major);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDTO(MajorDto majorDTO, @MappingTarget Major major);

    @Mapping(target = "id", ignore = true)
    MajorDto toInnerDTO(MajorRequestDto requestDTO);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorResponseDto toResponseDTO(MajorDto innerDTO);

    List<MajorResponseDto> toResponseDTOList(List<MajorDto> majors);
}