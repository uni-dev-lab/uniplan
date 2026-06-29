package org.unilab.uniplan.roomcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.category.CategoryRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.room.RoomRepository;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;

import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.CATEGORY_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.ROOM_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RoomCategoryValidator {
    private final CategoryRepository categoryRepository;
    private final RoomRepository roomRepository;

    public void validateForCreate(final RoomCategoryRequestDto requestDto) {
        validateCategoryExists(requestDto.categoryId());
        validateRoomExists(requestDto.roomId());
    }

    public void validateForUpdate(final UUID id, final RoomCategoryRequestDto requestDto) {
        validateCategoryExists(requestDto.categoryId());
        validateRoomExists(requestDto.roomId());
    }

    public void validateCategoryExists(final UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(
                CATEGORY_NOT_FOUND.getMessage(String.valueOf(categoryId))
            );
        }
    }

    public void validateRoomExists(final UUID roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(
                ROOM_NOT_FOUND.getMessage(String.valueOf(roomId))
            );
        }
    }
}

