package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomCategoryMapper roomCategoryMapper;

    @Transactional
    public Optional<RoomCategoryDto> createRoomCategory(final RoomCategoryDto dto) {
        RoomCategory entity = roomCategoryMapper.toEntity(dto);
        RoomCategory savedEntity = roomCategoryRepository.save(entity);
        return Optional.of(roomCategoryMapper.toDto(savedEntity));
    }

    public List<RoomCategoryDto> getAllRoomCategories() {
        List<RoomCategory> roomCategories = roomCategoryRepository.findAll();
        return roomCategoryMapper.toDtoList(roomCategories);
    }

    public Optional<RoomCategoryDto> getRoomCategoryById(final UUID roomId, final UUID categoryId) {
        RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);
        return roomCategoryRepository.findById(id)
                                     .map(roomCategoryMapper::toDto);
    }

    @Transactional
    public Optional<RoomCategoryDto> deleteRoomCategory(final UUID roomId, final UUID categoryId) {
        RoomCategoryId id = roomCategoryMapper.toRoomCategoryId(roomId, categoryId);
        Optional<RoomCategory> roomCategoryOpt = roomCategoryRepository.findById(id);

        if (roomCategoryOpt.isPresent()) {
            roomCategoryRepository.delete(roomCategoryOpt.get());
            return Optional.of(roomCategoryMapper.toDto(roomCategoryOpt.get()));
        }
        return Optional.empty();
    }
}
