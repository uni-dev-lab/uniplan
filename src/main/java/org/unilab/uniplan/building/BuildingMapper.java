package org.unilab.uniplan.building;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.building.dto.BuildingResponseDto;
import java.util.List;

@Mapper
public interface BuildingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university", ignore = true)
    Building toEntity(final BuildingRequestDto buildingRequestDto);

    @Mapping(source = "university.id", target = "universityId")
    @Mapping(source = "university.uniName", target = "universityName")
    BuildingResponseDto toResponseDto(final Building building);

    List<BuildingResponseDto> toResponseDtoList(final List<Building> buildings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university", ignore = true)
    void updateEntityFromDto(final BuildingRequestDto buildingRequestDto,
                             @MappingTarget final Building building);
}
