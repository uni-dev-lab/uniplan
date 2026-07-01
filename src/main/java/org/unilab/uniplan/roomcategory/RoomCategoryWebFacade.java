package org.unilab.uniplan.roomcategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.ROOM_CATEGORY_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomCategoryWebFacade {

    private final RoomCategoryService roomCategoryService;
    private final RoomCategoryMapper roomCategoryMapper;
    private final RoomCategoryValidator roomCategoryValidator;

    private RoomCategory getRoomCategoryOrThrow(final RoomCategoryId id) {
        return roomCategoryService.getById(id)
                             .orElseThrow(() -> new ResourceNotFoundException(ROOM_CATEGORY_NOT_FOUND.getMessage(
                                 String.valueOf(id))));
    }

    @Transactional
    public void createRoomCategory(final RoomCategoryRequestDto requestDto) {
        roomCategoryValidator.validateForCreate(requestDto);

        final RoomCategory roomCategory = roomCategoryMapper.toEntity(requestDto);
        roomCategoryService.save(roomCategory);

        log.info("created room category with roomId: {} and categoryId: {}",
                 requestDto.roomId(),
                 requestDto.categoryId());
    }

    @Transactional(readOnly = true)
    public List<RoomCategoryResponseDto> getAllRoomCategories() {
        return roomCategoryMapper.toResponseDtoList(roomCategoryService.getAll());
    }

    @Transactional
    public void deleteRoomCategory(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);
        final RoomCategory roomCategory = getRoomCategoryOrThrow(id);

        roomCategoryService.delete(roomCategory);

        log.info("deleted room category with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public RoomCategoryResponseDto getRoomCategoryById(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);
        final RoomCategory roomCategory = getRoomCategoryOrThrow(id);

        return roomCategoryMapper.toResponseDto(roomCategory);
    }

    @Transactional
    public void updateRoomCategory(final UUID roomId, final UUID categoryId, final RoomCategoryRequestDto requestDto) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);

        roomCategoryValidator.validateForUpdate(id, requestDto);

        final RoomCategory roomCategory = getRoomCategoryOrThrow(id);
        roomCategoryMapper.updateEntity(requestDto, roomCategory);
        roomCategoryService.save(roomCategory);

        log.info("updated room category with ID: {}", roomCategory.getId());
    }
}
