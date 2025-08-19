package org.unilab.uniplan.roomcategory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;

@ExtendWith(MockitoExtension.class)
class RoomCategoryServiceTest {

    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @Mock
    private RoomCategoryMapper roomCategoryMapper;

    @InjectMocks
    private RoomCategoryService roomCategoryService;

    private UUID roomId;
    private UUID categoryId;
    private RoomCategoryDto dto;
    private RoomCategory entity;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        dto = new RoomCategoryDto(roomId, categoryId);
        entity = new RoomCategory();
    }

    @Test
    void testCreateRoomCategoryShouldSaveAndReturnDto() {
        when(roomCategoryMapper.toEntity(dto)).thenReturn(entity);
        when(roomCategoryRepository.save(entity)).thenReturn(entity);
        when(roomCategoryMapper.toDto(entity)).thenReturn(dto);

        RoomCategoryDto result = roomCategoryService.createRoomCategory(dto);

        assertEquals(dto, result);
    }

    @Test
    void testGetAllRoomCategoriesShouldReturnListOfRoomCategoryDtos() {
        List<RoomCategory> entities = List.of(entity);
        List<RoomCategoryDto> dtos = List.of(dto);

        when(roomCategoryRepository.findAll()).thenReturn(entities);
        when(roomCategoryMapper.toDtoList(entities)).thenReturn(dtos);

        List<RoomCategoryDto> result = roomCategoryService.getAllRoomCategories();

        assertEquals(dtos, result);
    }

    @Test
    void testGetRoomCategoryByIdShouldReturnRoomCategoryDtoIfFound() {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(roomCategoryMapper.toDto(entity)).thenReturn(dto);

        RoomCategoryDto result = roomCategoryService.getRoomCategoryById(roomId,
                                                                                   categoryId);
        assertEquals(dto, result);
    }

    @Test
    void testGetRoomCategoryByIdShouldReturnEmptyOptionalIfNotFound() {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomCategoryService.getRoomCategoryById(roomId,
                                                                                      categoryId));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testDeleteRoomCategoryShouldDeleteIfFound() {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(roomCategoryRepository).delete(entity);

        assertDoesNotThrow(() -> roomCategoryService.deleteRoomCategory(roomId, categoryId));
        verify(roomCategoryRepository).delete(entity);
    }

    @Test
    void testDeleteRoomCategoryShouldThrowIfNotFound() {
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            roomCategoryService.deleteRoomCategory(roomId, categoryId));

        assertTrue(exception.getMessage().contains(String.valueOf(roomId)));
        assertTrue(exception.getMessage().contains(String.valueOf(categoryId)));
    }
}
