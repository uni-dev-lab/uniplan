package org.unilab.uniplan.room;

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
import org.unilab.uniplan.room.dto.RoomDto;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    private UUID id;
    private UUID facultyId;
    private RoomDto dto;
    private Room entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        dto = new RoomDto(id, facultyId, "101");
        entity = new Room();
    }

    @Test
    void testCreateRoomShouldSaveAndReturnDto() {
        when(roomMapper.toEntity(dto)).thenReturn(entity);
        when(roomRepository.save(entity)).thenReturn(entity);
        when(roomMapper.toDto(entity)).thenReturn(dto);

        RoomDto result = roomService.createRoom(dto);

        assertEquals(dto, result);
    }

    @Test
    void testGetAllRoomsShouldReturnListOfRoomDtos() {
        List<Room> entities = List.of(entity);
        List<RoomDto> dtos = List.of(dto);

        when(roomRepository.findAll()).thenReturn(entities);
        when(roomMapper.toDtoList(entities)).thenReturn(dtos);

        List<RoomDto> result = roomService.getAllRooms();

        assertEquals(dtos, result);
    }

    @Test
    void testGetRoomByIdShouldReturnRoomDtoIfFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.of(entity));
        when(roomMapper.toDto(entity)).thenReturn(dto);

        RoomDto result = roomService.getRoomById(id);

        assertEquals(dto, result);
    }

    @Test
    void testGetRoomByIdShouldReturnEmptyOptionalIfRoomNotFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }

    @Test
    void testUpdateRoomShouldUpdateAndReturnDtoIfFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(roomMapper).updateEntityFromDto(dto, entity);
        when(roomRepository.save(entity)).thenReturn(entity);
        when(roomMapper.toDto(entity)).thenReturn(dto);

        RoomDto result = roomService.updateRoom(id, dto);

        assertEquals(dto, result);
    }

    @Test
    void testUpdateRoomShouldReturnEmptyOptionalIfNotFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> roomService.updateRoom(id, dto));

        assertTrue(exception.getMessage().contains(id.toString()));
    }

    @Test
    void testDeleteRoomShouldDeleteRoomIfFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(roomRepository).delete(entity);

        assertDoesNotThrow(() -> roomService.deleteRoom(id));
        verify(roomRepository).delete(entity);
    }

    @Test
    void testDeleteRoomShouldThrowIfNotFound() {
        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            roomService.deleteRoom(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}
