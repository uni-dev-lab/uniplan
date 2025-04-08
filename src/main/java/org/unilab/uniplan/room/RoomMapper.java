package org.unilab.uniplan.room;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    RoomDto toDto(Room room);

    @Mapping(source = "facultyId", target = "faculty.id")
    Room toEntity(RoomDto dto);
}
