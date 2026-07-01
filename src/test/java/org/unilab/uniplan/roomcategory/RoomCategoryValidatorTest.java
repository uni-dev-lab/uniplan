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
    void testValidateForCreateShouldPassWhenCategoryAndRoomExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void testValidateForCreateShouldThrowWhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void testValidateForCreateShouldThrowWhenRoomDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForCreate(requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void testValidateForUpdateShouldPassWhenCategoryAndRoomExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void testValidateForUpdateShouldThrowWhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void testValidateForUpdateShouldThrowWhenRoomDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateForUpdate(id, requestDto));

        verify(categoryRepository).existsById(categoryId);
        verify(roomRepository).existsById(roomId);
    }

    @Test
    void testValidateCategoryExistsShouldPassWhenCategoryExists() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateCategoryExists(categoryId));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void testValidateCategoryExistsShouldThrowWhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateCategoryExists(categoryId));

        verify(categoryRepository).existsById(categoryId);
    }

    @Test
    void testValidateRoomExistsShouldPassWhenRoomExists() {
        when(roomRepository.existsById(roomId)).thenReturn(true);

        assertDoesNotThrow(() -> roomCategoryValidator.validateRoomExists(roomId));

        verify(roomRepository).existsById(roomId);
    }
    
    @Test
    void testValidateRoomExistsShouldThrowWhenRoomDoesNotExist() {
        when(roomRepository.existsById(roomId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryValidator.validateRoomExists(roomId));

        verify(roomRepository).existsById(roomId);
    }
}