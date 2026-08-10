package org.unilab.uniplan.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @InjectMocks
    private RoomService roomService;
    private Room room;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        room = new Room();
    }

    @Test
    void save_shouldSaveEntity() {
        when(roomRepository.save(room)).thenReturn(room);

        roomService.save(room);

        verify(roomRepository).save(room);
    }

    @Test
    void findAll_shouldReturnListOfEntities() {
        List<Room> rooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> result = roomService.getAll();

        verify(roomRepository).findAll();
        assertEquals(result, rooms);
    }

    @Test
    void findById_shouldReturnEntity_whenRoomExists() {
        Optional<Room> expected = Optional.of(room);
        when(roomRepository.findById(id)).thenReturn(expected);

        Optional<Room> result = roomService.getById(id);

        verify(roomRepository).findById(id);
        assertEquals(expected, result);
    }

    @Test
    void delete_shouldDeleteEntity_whenFacultyExists() {
        roomService.delete(room);

        verify(roomRepository).delete(room);
    }

}
