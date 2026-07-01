package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@Mapper
public interface RoomCategoryMapper {

    @Mapping(target = "id", expression = "java(new RoomCategoryId(dto.roomId(), dto.categoryId()))")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "category", ignore = true)
    RoomCategory toEntity(RoomCategoryRequestDto dto);

    @Mapping(source = "id.roomId", target = "roomId")
    @Mapping(source = "id.categoryId", target = "categoryId")
    RoomCategoryResponseDto toResponseDto(final RoomCategory roomCategory);

    List<RoomCategoryResponseDto> toResponseDtoList(List<RoomCategory> roomCategories);

    @Mapping(target = "id", expression = "java(new RoomCategoryId(requestDto.roomId(), requestDto.categoryId()))")
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntity(final RoomCategoryRequestDto requestDto,
                      @MappingTarget final RoomCategory entity);

    RoomCategoryId toRoomCategoryId(UUID roomId, UUID categoryId);
}
