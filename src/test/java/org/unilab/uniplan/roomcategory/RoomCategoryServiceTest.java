package org.unilab.uniplan.roomcategory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;

@ExtendWith(MockitoExtension.class)
class RoomCategoryServiceTest {

    @Mock
    private RoomCategoryRepository roomCategoryRepository;

    @Mock
    private RoomCategoryMapper roomCategoryMapper;

    @InjectMocks
    private RoomCategoryService roomCategoryService;

    @Test
    void testCreateRoomCategoryShouldSaveAndReturnDto() {
        UUID roomId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        RoomCategoryDto dto = new RoomCategoryDto(roomId, categoryId);
        RoomCategory entity = new RoomCategory();
        RoomCategory saved = new RoomCategory();
        RoomCategoryDto savedDto = new RoomCategoryDto(roomId, categoryId);

        when(roomCategoryMapper.toEntity(dto)).thenReturn(entity);
        when(roomCategoryRepository.save(entity)).thenReturn(saved);
        when(roomCategoryMapper.toDto(saved)).thenReturn(savedDto);

        RoomCategoryDto result = roomCategoryService.createRoomCategory(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testGetAllRoomCategoriesShouldReturnListOfRoomCategoryDtos() {
        List<RoomCategory> entities = List.of(new RoomCategory());
        List<RoomCategoryDto> dtos = List.of(new RoomCategoryDto(UUID.randomUUID(),
                                                                 UUID.randomUUID()));

        when(roomCategoryRepository.findAll()).thenReturn(entities);
        when(roomCategoryMapper.toDtoList(entities)).thenReturn(dtos);

        List<RoomCategoryDto> result = roomCategoryService.getAllRoomCategories();

        assertEquals(dtos, result);
    }

    @Test
    void testGetRoomCategoryByIdShouldReturnRoomCategoryDtoIfFound() {
        UUID roomId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);
        RoomCategory entity = new RoomCategory();
        RoomCategoryDto dto = new RoomCategoryDto(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.of(entity));
        when(roomCategoryMapper.toDto(entity)).thenReturn(dto);

        Optional<RoomCategoryDto> result = roomCategoryService.getRoomCategoryById(roomId,
                                                                                   categoryId);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetRoomCategoryByIdShouldReturnEmptyOptionalIfNotFound() {
        UUID roomId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RoomCategoryDto> result = roomCategoryService.getRoomCategoryById(roomId,
                                                                                   categoryId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteRoomCategoryShouldDeleteIfFound() {
        UUID roomId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);
        RoomCategory entity = new RoomCategory();

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(roomCategoryRepository).delete(entity);

        assertDoesNotThrow(() -> roomCategoryService.deleteRoomCategory(roomId, categoryId));
        verify(roomCategoryRepository).delete(entity);
    }

    @Test
    void testDeleteRoomCategoryShouldThrowIfNotFound() {
        UUID roomId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        RoomCategoryId id = new RoomCategoryId(roomId, categoryId);

        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            roomCategoryService.deleteRoomCategory(roomId, categoryId));

        assertTrue(exception.getMessage().contains(roomId.toString()));
        assertTrue(exception.getMessage().contains(categoryId.toString()));
    }
}