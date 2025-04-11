package org.unilab.uniplan.roomcategory;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.category.Category;
import org.unilab.uniplan.category.CategoryService;
import org.unilab.uniplan.room.Room;
import org.unilab.uniplan.room.RoomService;

@Service
public class RoomCategoryService {

    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomCategoryMapper roomCategoryMapper;
    private final RoomService roomService;
    private final CategoryService categoryService;

    @Autowired
    public RoomCategoryService(
        RoomCategoryRepository roomCategoryRepository,
        RoomCategoryMapper roomCategoryMapper,
        RoomService roomService,
        CategoryService categoryService
    ) {
        this.roomCategoryRepository = roomCategoryRepository;
        this.roomCategoryMapper = roomCategoryMapper;
        this.roomService = roomService;
        this.categoryService = categoryService;
    }

    @Transactional
    public RoomCategoryDto createRoomCategory(RoomCategoryDto dto) {
        Room room = roomService.getRoomEntity(dto.roomId());
        Category category = categoryService.getCategoryEntity(dto.categoryId());

        RoomCategory entity = roomCategoryMapper.toEntity(dto);
        entity.setRoom(room);
        entity.setCategory(category);

        return roomCategoryMapper.toDto(roomCategoryRepository.save(entity));
    }

    public List<RoomCategoryDto> getAllRoomCategories() {
        return roomCategoryRepository.findAll()
                                     .stream()
                                     .map(roomCategoryMapper::toDto)
                                     .toList();
    }

    public RoomCategoryDto getRoomCategoryById(UUID roomId, UUID categoryId) {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        return roomCategoryRepository.findById(id)
                                     .map(roomCategoryMapper::toDto)
                                     .orElseThrow();
    }

    @Transactional
    public RoomCategoryDto updateRoomCategory(RoomCategoryDto dto) {
        RoomCategoryId id = new RoomCategoryId(dto.roomId(), dto.categoryId());
        RoomCategory existing = roomCategoryRepository.findById(id).orElseThrow();

        Room room = roomService.getRoomEntity(dto.roomId());
        Category category = categoryService.getCategoryEntity(dto.categoryId());

        existing.setRoom(room);
        existing.setCategory(category);

        return roomCategoryMapper.toDto(roomCategoryRepository.save(existing));
    }

    @Transactional
    public void deleteRoomCategory(UUID roomId, UUID categoryId) {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);
        roomCategoryRepository.deleteById(id);
    }
}
