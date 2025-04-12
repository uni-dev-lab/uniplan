package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomCategoryMapper {

    @Mapping(target = "id", expression = "java(new RoomCategoryId(dto.roomId(), dto.categoryId()))")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "category", ignore = true)
    RoomCategory toEntity(RoomCategoryDto dto);

    @Mapping(source = "id.roomId", target = "roomId")
    @Mapping(source = "id.categoryId", target = "categoryId")
    RoomCategoryDto toDto(RoomCategory entity);

    default RoomCategoryId toRoomCategoryId(UUID roomId, UUID categoryId) {
        return new RoomCategoryId(roomId, categoryId);
    }

    List<RoomCategoryDto> toDtoList(List<RoomCategory> entities);
}
