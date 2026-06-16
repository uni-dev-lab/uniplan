package org.unilab.uniplan.university;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

@Mapper
public interface UniversityMapper {

    @Mapping(target = "id", ignore = true)
    University createEntity(UniversityRequestDto request);

    UniversityResponseDto toResponseDto(University university);

    List<UniversityResponseDto> toResponseDtoList(List<University> universities);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UniversityRequestDto request,
                      @MappingTarget University university);
}
