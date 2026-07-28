package org.unilab.uniplan.roomcategory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.category.CategoryRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.room.RoomRepository;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;

@ExtendWith(MockitoExtension.class)
class RoomCategoryValidatorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomCategoryValidator roomCategoryValidator;

    private UUID roomId;
    private UUID categoryId;
    private RoomCategoryId id;
    private RoomCategoryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        id = new RoomCategoryId(roomId, categoryId);
        requestDto = new RoomCategoryRequestDto(roomId, categoryId);
    }

    @Test
    void validateForCreate_shouldPass_whenCategoryAndRoomExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void ValidateForCreateShouldThrowWhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void validateForCreate_shouldThrow_whenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void validateForUpdate_shouldPass_whenCategoryAndRoomExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void validateForUpdate_shouldThrow_whenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void validateForUpdate_shouldThrow_whenRoomDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void validateCategoryExists_shouldPass_whenCategoryExists() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateCategoryExists(categoryId));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void validateCategoryExists_shouldThrow_whenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateCategoryExists(categoryId));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void validateRoomExists_shouldPass_whenRoomExists() {
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateRoomExists(roomId));

        verify(roomRepository).existsById(roomId);
    }
    
    @Test
    void validateRoomExists_shouldThrow_whenRoomDoesNotExist() {
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateRoomExists(roomId));

        verify(roomRepository).existsById(roomId);
    }
}