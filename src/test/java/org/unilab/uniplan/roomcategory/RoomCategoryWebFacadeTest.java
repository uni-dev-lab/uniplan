package org.unilab.uniplan.roomcategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@ExtendWith(MockitoExtension.class)
class RoomCategoryWebFacadeTest {

    @Mock
    private RoomCategoryService roomCategoryService;

    @Mock
    private RoomCategoryMapper roomCategoryMapper;

    @Mock
    private RoomCategoryValidator roomCategoryValidator;

    @InjectMocks
    private RoomCategoryWebFacade roomCategoryWebFacade;

    private UUID roomId;
    private UUID categoryId;
    private RoomCategoryId id;
    private RoomCategory roomCategory;
    private RoomCategoryRequestDto requestDto;
    private RoomCategoryResponseDto responseDto;

    @BeforeEach
    void setUp() {
        roomId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        id = new RoomCategoryId(roomId, categoryId);
        roomCategory = new RoomCategory();
        requestDto = mock(RoomCategoryRequestDto.class);
        responseDto = mock(RoomCategoryResponseDto.class);
    }

    @Test
    void testCreateRoomCategoryShouldValidateMapAndSaveRoomCategory() {
        when(roomCategoryMapper.toEntity(requestDto)).thenReturn(roomCategory);

        roomCategoryWebFacade.createRoomCategory(requestDto);

        final InOrder inOrder = inOrder(roomCategoryValidator, roomCategoryService);
        inOrder.verify(roomCategoryValidator).validateForCreate(requestDto);
        inOrder.verify(roomCategoryService).save(roomCategory);

        verify(roomCategoryMapper).toEntity(requestDto);
    }

    @Test
    void testGetAllRoomCategoriesShouldReturnResponseDtoList() {
        final List<RoomCategory> roomCategories = List.of(roomCategory);
        final List<RoomCategoryResponseDto> responseDtos = List.of(responseDto);

        when(roomCategoryService.getAll()).thenReturn(roomCategories);
        when(roomCategoryMapper.toResponseDtoList(roomCategories)).thenReturn(responseDtos);

        final List<RoomCategoryResponseDto> result = roomCategoryWebFacade.getAllRoomCategories();

        assertEquals(responseDtos, result);
        verify(roomCategoryService).getAll();
        verify(roomCategoryMapper).toResponseDtoList(roomCategories);
    }

    @Test
    void testGetRoomCategoryByIdShouldReturnResponseDtoIfFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.of(roomCategory));
        when(roomCategoryMapper.toResponseDto(roomCategory)).thenReturn(responseDto);

        final RoomCategoryResponseDto result = roomCategoryWebFacade.getRoomCategoryById(roomId, categoryId);

        assertEquals(responseDto, result);
        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryService).getById(id);
        verify(roomCategoryMapper).toResponseDto(roomCategory);
    }

    @Test
    void testGetRoomCategoryByIdShouldThrowIfNotFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryWebFacade.getRoomCategoryById(roomId, categoryId));

        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryService).getById(id);
        verify(roomCategoryMapper, never()).toResponseDto(any(RoomCategory.class));
    }

    @Test
    void testUpdateRoomCategoryShouldValidateUpdateAndSaveRoomCategoryIfFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.of(roomCategory));

        roomCategoryWebFacade.updateRoomCategory(roomId, categoryId, requestDto);

        final InOrder inOrder = inOrder(roomCategoryValidator, roomCategoryService);
        inOrder.verify(roomCategoryValidator).validateForUpdate(id, requestDto);
        inOrder.verify(roomCategoryService).getById(id);
        inOrder.verify(roomCategoryService).save(roomCategory);

        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryMapper).updateEntity(requestDto, roomCategory);
    }

    @Test
    void testUpdateRoomCategoryShouldThrowIfNotFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryWebFacade.updateRoomCategory(roomId, categoryId, requestDto));

        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryValidator).validateForUpdate(id, requestDto);
        verify(roomCategoryService).getById(id);
        verify(roomCategoryMapper, never()).updateEntity(any(RoomCategoryRequestDto.class), any(RoomCategory.class));
        verify(roomCategoryService, never()).save(any(RoomCategory.class));
    }

    @Test
    void testDeleteRoomCategoryShouldDeleteRoomCategoryIfFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.of(roomCategory));

        roomCategoryWebFacade.deleteRoomCategory(roomId, categoryId);

        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryService).getById(id);
        verify(roomCategoryService).delete(roomCategory);
    }

    @Test
    void testDeleteRoomCategoryShouldThrowIfNotFound() {
        when(roomCategoryMapper.toRoomCategoryId(roomId, categoryId)).thenReturn(id);
        when(roomCategoryService.getById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> roomCategoryWebFacade.deleteRoomCategory(roomId, categoryId));

        verify(roomCategoryMapper).toRoomCategoryId(roomId, categoryId);
        verify(roomCategoryService).getById(id);
        verify(roomCategoryService, never()).delete(any(RoomCategory.class));
    }
}