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

    University toEntity(final UniversityDto universityDto);

    UniversityDto toDto(final University university);

    UniversityDto toInternalDto(final UniversityRequestDto universityRequestDto);

    UniversityResponseDto toResponseDto(final UniversityDto universityDto);

    List<UniversityDto> toDtoList(final List<University> universities);

    List<UniversityResponseDto> toResponseDtoList(final List<UniversityDto> universities);

    @Mapping(target = "uniName", source = "universityDto.uniName")
    @Mapping(target = "location", source = "universityDto.location")
    @Mapping(target = "accreditation", source = "universityDto.accreditation")
    @Mapping(target = "website", source = "universityDto.website")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final UniversityDto universityDto,
                             @MappingTarget final University university);
}
