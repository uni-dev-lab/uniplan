package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@Mapper
public interface RoomCategoryMapper {

    @Mapping(target = "id", expression = "java(new RoomCategoryId(dto.roomId(), dto.categoryId()))")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "category", ignore = true)
    RoomCategory toEntity(RoomCategoryDto dto);

    @Mapping(source = "id.roomId", target = "roomId")
    @Mapping(source = "id.categoryId", target = "categoryId")
    RoomCategoryDto toDto(RoomCategory entity);
    
    RoomCategoryDto toInternalDto(RoomCategoryRequestDto requestDto);

    RoomCategoryResponseDto toResponseDto(RoomCategoryDto roomCategoryDto);

    List<RoomCategoryDto> toDtoList(List<RoomCategory> entities);

    List<RoomCategoryResponseDto> toResponseDtoList(List<RoomCategoryDto> roomCategories);

    default RoomCategoryId toRoomCategoryId(UUID roomId, UUID categoryId) {
        return new RoomCategoryId(roomId, categoryId);
    }
}
