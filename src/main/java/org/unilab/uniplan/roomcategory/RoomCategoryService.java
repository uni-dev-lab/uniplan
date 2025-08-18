package org.unilab.uniplan.roomcategory;

import static org.unilab.uniplan.utils.ErrorConstants.ROOM_CATEGORY_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;

@Service
@RequiredArgsConstructor
public class RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomCategoryMapper roomCategoryMapper;

    @Transactional
    public RoomCategoryDto createRoomCategory(final RoomCategoryDto roomCategoryDto) {
        final RoomCategory roomCategory = roomCategoryMapper.toEntity(roomCategoryDto);

        return roomCategoryMapper.toDto(roomCategoryRepository.save(roomCategory));
    }

    public List<RoomCategoryDto> getAllRoomCategories() {
        final List<RoomCategory> roomCategories = roomCategoryRepository.findAll();
        return roomCategoryMapper.toDtoList(roomCategories);
    }

    public RoomCategoryDto getRoomCategoryById(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);

        return roomCategoryRepository.findById(id)
                                     .map(roomCategoryMapper::toDto)
                                     .orElseThrow(() -> new ResourceNotFoundException(
                                         ROOM_CATEGORY_NOT_FOUND.getMessage(String.valueOf(id))));
    }

    @Transactional
    public void deleteRoomCategory(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);

        final RoomCategory roomCategory = roomCategoryRepository.findById(id)
                                                                .orElseThrow(() -> new ResourceNotFoundException(
                                                                    ROOM_CATEGORY_NOT_FOUND.getMessage(
                                                                        String.valueOf(id))));

        roomCategoryRepository.delete(roomCategory);
    }
}
