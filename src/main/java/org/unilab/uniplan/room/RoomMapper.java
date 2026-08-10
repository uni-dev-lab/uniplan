package org.unilab.uniplan.room;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@Mapper
public interface RoomMapper {

    @Mapping(target = "faculty", ignore = true)
    Room toEntity(final RoomRequestDto roomRequestDto);

    @Mapping(target = "facultyId", source = "faculty.id")
    RoomResponseDto toResponseDto(Room room);

    List<RoomResponseDto> toResponseDtoList(final List<Room> rooms);

    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final RoomRequestDto roomRequestDto, @MappingTarget final Room room);
}
