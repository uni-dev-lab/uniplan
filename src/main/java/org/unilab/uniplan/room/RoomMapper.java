package org.unilab.uniplan.room;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.room.dto.RoomDto;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@Mapper
public interface RoomMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    RoomDto toDto(final Room room);

    @Mapping(source = "facultyId", target = "faculty.id")
    Room toEntity(final RoomDto roomDto);

    @Mapping(source = "facultyId", target = "facultyId")
    @Mapping(target = "id", ignore = true)
    RoomDto toInternalDto(final RoomRequestDto roomRequestDto);

    @Mapping(source = "facultyId", target = "facultyId")
    RoomResponseDto toResponseDto(final RoomDto roomDto);

    List<RoomDto> toDtoList(final List<Room> rooms);

    List<RoomResponseDto> toResponseDtoList(final List<RoomDto> rooms);

    @Mapping(source = "roomDto.facultyId", target = "faculty.id")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final RoomDto roomDto, @MappingTarget final Room room);
}
