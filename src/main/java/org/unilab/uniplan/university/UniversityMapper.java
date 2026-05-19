package org.unilab.uniplan.university;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.university.dto.UniversityDto;
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

@Mapper
public interface UniversityMapper {
    @Mapping(target = "id", ignore = true)
    University toEntity(UniversityRequestDto request);

    UniversityResponseDto toResponseDto(University university);

    UniversityDto toDto(final University university);

    @Mapping(target = "id", ignore = true)
    UniversityDto toInternalDto(final UniversityRequestDto universityRequestDto);

    List<UniversityDto> toDtoList(final List<University> universities);

    List<UniversityResponseDto> toResponseDtoList(final List<University> universities);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequestDto(final UniversityRequestDto request,
                             @MappingTarget final University university);
}
