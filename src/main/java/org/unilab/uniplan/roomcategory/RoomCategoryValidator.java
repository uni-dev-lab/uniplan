package org.unilab.uniplan.roomcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.category.CategoryRepository;
import org.unilab.uniplan.exception.CategoryNotFoundException;
import org.unilab.uniplan.exception.RoomNotFoundException;
import org.unilab.uniplan.room.RoomRepository;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomCategoryValidator {
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;

    public void validateForCreate(final RoomCategoryRequestDto requestDto) {
        validateCategoryExists(requestDto.categoryId());
        validateRoomExists(requestDto.roomId());
    }

    public void validateForUpdate(final RoomCategoryId id, final RoomCategoryRequestDto requestDto) {
        validateCategoryExists(requestDto.categoryId());
        validateRoomExists(requestDto.roomId());
    }

    public void validateCategoryExists(final UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
    }

    public void validateRoomExists(final UUID roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new RoomNotFoundException(roomId);
        }
    }
}

