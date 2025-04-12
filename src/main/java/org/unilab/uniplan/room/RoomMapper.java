package org.unilab.uniplan.room;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = FacultyMapper.class)
public interface RoomMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    @Mapping(source = "roomNumber", target = "roomNumber")
    RoomDto toDto(final Room room);

    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(source = "roomNumber", target = "roomNumber")
    Room toEntity(final RoomDto dto);

    List<RoomDto> toDtoList(final List<Room> rooms);

    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(source = "roomNumber", target = "roomNumber")
    void updateEntityFromDto(final RoomDto dto, @MappingTarget final Room room);

    Room map(final UUID value);
}
