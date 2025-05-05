package org.unilab.uniplan.roomcategory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;

@Service
@RequiredArgsConstructor
public class RoomCategoryService {

    private static final String ROOM_CATEGORY_NOT_FOUND = "RoomCategory with Room ID {0} and Category ID {1} not found.";

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

    public Optional<RoomCategoryDto> getRoomCategoryById(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);

        return roomCategoryRepository.findById(id)
                                     .map(roomCategoryMapper::toDto);
    }

    @Transactional
    public void deleteRoomCategory(final UUID roomId, final UUID categoryId) {
        final RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);

        final RoomCategory roomCategory = roomCategoryRepository.findById(id)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                    MessageFormat.format(
                                                                        ROOM_CATEGORY_NOT_FOUND,
                                                                        roomId,
                                                                        categoryId)));

        roomCategoryRepository.delete(roomCategory);
    }
}
