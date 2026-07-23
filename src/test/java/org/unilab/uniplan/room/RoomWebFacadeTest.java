package org.unilab.uniplan.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;
import org.unilab.uniplan.university.University;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomWebFacadeTest {
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private RoomService roomService;
    @InjectMocks
    private RoomWebFacade roomWebFacade;
    private RoomRequestDto requestDto;
    private Room entity;
    private RoomResponseDto responseDto;
    private UUID id;
private UUID categoryId;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
         categoryId = UUID.randomUUID();
        University university = new University("Sofia University",
                                               "Sofia, Bulgaria",
                                               (short) 1888,
                                               "NEAA",
                                               "https://uni-sofia.bg", null
        );
        Faculty faculty = new Faculty(university, "FMI", "Faculty Name");
        faculty.setId(facultyId);
        String roomNumber = "111";
        requestDto = new RoomRequestDto(facultyId, "222", 20);
        entity = new Room(faculty, roomNumber, 20);
        entity.setId(id);
        responseDto = new RoomResponseDto(id, facultyId, roomNumber, 20, categoryId);
    }

    @Test
    void createRoom_shouldSaveRoom_whenRequestIsValid() {
        when(roomMapper.toEntity(requestDto)).thenReturn(entity);

        roomWebFacade.createRoom(requestDto);

        final var inOrder = inOrder(roomMapper, roomService);
        inOrder.verify(roomMapper).toEntity(requestDto);
        inOrder.verify(roomService).save(entity);
    }

    @Test
    void getAllRooms_shouldReturnListOfResponseDtos() {
        List<RoomResponseDto> rooms = List.of(responseDto);
        when(roomService.getAllRoomResponses()).thenReturn(rooms);

        List<RoomResponseDto> results = roomWebFacade.getAllRooms();

        assertEquals(rooms, results);
        assertThat(results.getFirst().categoryId()).isEqualTo(categoryId);
        verify(roomService).getAllRoomResponses();
    }

    @Test
    void getRoomById_shouldReturnResponseDto_whenRoomExists() {
        when(roomService.getRoomResponseById(id)).thenReturn(Optional.of(responseDto));

        RoomResponseDto result = roomWebFacade.getRoomById(id);

        assertEquals(result, responseDto);
        verify(roomService).getRoomResponseById(id);
    }

    @Test
    void deleteRoom_shouldDeleteRoom_whenRoomExists() {
        when(roomService.getById(id)).thenReturn(Optional.of(entity));

        roomWebFacade.deleteRoom(entity.getId());

        verify(roomService).delete(entity);
    }

    @Test
    void deleteRoom_shouldThrowResourceNotFoundException_whenRoomNotFound() {
        when(roomService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomWebFacade.deleteRoom(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void updateRoom_shouldUpdateRoom_whenRoomExists() {
        when(roomService.getById(id)).thenReturn(Optional.of(entity));

        roomWebFacade.updateRoom(id, requestDto);

        verify(roomMapper).updateEntityFromDto(requestDto, entity);
        verify(roomService).save(entity);
    }

    @Test
    void updateRoom_shouldThrowResourceNotFoundException_whenRoomNotFound() {
        when(roomService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomWebFacade.updateRoom(id, requestDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(id.toString());
    }
}
