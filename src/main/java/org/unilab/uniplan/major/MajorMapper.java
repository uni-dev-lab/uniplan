package org.unilab.uniplan.major;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface MajorMapper {
    
    @Mapping(source = "facultyId", target = "faculty.id")
    Major toEntity(MajorDTO majorDTO);

    @Mapping(source = "faculty.id", target = "facultyId")
    MajorDTO toDTO(Major major);

    @Mapping(source = "facultyId", target = "faculty.id")
    void updateEntityFromDTO(MajorDTO majorDTO, @MappingTarget Major major);

    @Mapping(target = "id", ignore = true)
    MajorDTO toInnerDTO(MajorRequestDTO requestDTO);

    @Mapping(source = "facultyId", target = "facultyId")
    MajorResponseDTO toResponseDTO(MajorDTO innerDTO);

    List<MajorResponseDTO> toResponseDTOList(List<MajorDTO> majors);
}