package org.unilab.uniplan.room;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
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
import org.unilab.uniplan.room.dto.RoomDto;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void testCreateRoomShouldSaveAndReturnDto() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        String roomNumber = "101";

        RoomDto dto = new RoomDto(id, facultyId, roomNumber);
        Room entity = new Room();
        Room saved = new Room();
        RoomDto savedDto = new RoomDto(id, facultyId, roomNumber);

        when(roomMapper.toEntity(dto)).thenReturn(entity);
        when(roomRepository.save(entity)).thenReturn(saved);
        when(roomMapper.toDto(saved)).thenReturn(savedDto);

        RoomDto result = roomService.createRoom(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testGetAllRoomsShouldReturnListOfRoomDtos() {
        List<Room> entities = List.of(new Room());
        List<RoomDto> dtos = List.of(new RoomDto(UUID.randomUUID(), UUID.randomUUID(), "102"));

        when(roomRepository.findAll()).thenReturn(entities);
        when(roomMapper.toDtoList(entities)).thenReturn(dtos);

        List<RoomDto> result = roomService.getAllRooms();

        assertEquals(dtos, result);
    }

    @Test
    void testGetRoomByIdShouldReturnRoomDtoIfFound() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        Room entity = new Room();
        RoomDto dto = new RoomDto(id, facultyId, "201");

        when(roomRepository.findById(id)).thenReturn(Optional.of(entity));
        when(roomMapper.toDto(entity)).thenReturn(dto);

        Optional<RoomDto> result = roomService.getRoomById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testGetRoomByIdShouldReturnEmptyOptionalIfRoomNotFound() {
        UUID id = UUID.randomUUID();

        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RoomDto> result = roomService.getRoomById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateRoomShouldUpdateAndReturnDtoIfFound() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        String updatedRoomNumber = "101A";

        RoomDto dto = new RoomDto(id, facultyId, updatedRoomNumber);
        Room existing = new Room();
        Room updated = new Room();
        RoomDto updatedDto = new RoomDto(id, facultyId, updatedRoomNumber);

        when(roomRepository.findById(id)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> null).when(roomMapper).updateEntityFromDto(dto, existing);
        when(roomRepository.save(existing)).thenReturn(updated);
        when(roomMapper.toDto(updated)).thenReturn(updatedDto);

        Optional<RoomDto> result = roomService.updateRoom(id, dto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }

    @Test
    void testUpdateRoomShouldReturnEmptyOptionalIfNotFound() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        RoomDto dto = new RoomDto(id, facultyId, "301B");

        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        Optional<RoomDto> result = roomService.updateRoom(id, dto);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteRoomShouldDeleteRoomIfFound() {
        UUID id = UUID.randomUUID();
        Room entity = new Room();

        when(roomRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(roomRepository).delete(entity);

        assertDoesNotThrow(() -> roomService.deleteRoom(id));
        verify(roomRepository).delete(entity);
    }

    @Test
    void testDeleteRoomShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(roomRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            roomService.deleteRoom(id));

        assertTrue(exception.getMessage().contains(id.toString()));
    }
}