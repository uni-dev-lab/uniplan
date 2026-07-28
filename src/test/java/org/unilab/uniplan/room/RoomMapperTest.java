package org.unilab.uniplan.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;
import org.unilab.uniplan.university.University;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class RoomMapperTest {

    private final RoomMapper roomMapper = new RoomMapperImpl();
    private Room room1;
    private Room room2;
    private RoomRequestDto roomRequestDto;
    private RoomResponseDto roomResponseDto1;
    private RoomResponseDto roomResponseDto2;

    private String roomNumber1;

    @BeforeEach
    void setUp() {
        University university = new University("Sofia University",
                                               "Sofia, Bulgaria",
                                               (short) 1888,
                                               "NEAA",
                                               "https://uni-sofia.bg",
                                        null
        );
        roomNumber1 = "111";
        String roomNumber2 = "222";
        room1 = new Room(null, roomNumber1, 20, null);
        room1.setId(UUID.randomUUID());
        room2 = new Room(null, roomNumber2, 20, null);
        room2.setId(UUID.randomUUID());
        roomRequestDto = new RoomRequestDto(null, roomNumber1, 20);
        roomResponseDto1 = new RoomResponseDto(room1.getId(), null, roomNumber1,20,null, null);
        roomResponseDto2 = new RoomResponseDto(room2.getId(), null, roomNumber2,20,null, null);

    }

    @Test
    void toEntity_shouldMapAllFieldsAndIgnoreId_whenRequestDtoIsValid() {
        final Room result = roomMapper.toEntity(roomRequestDto);
        assertThat(result.getRoomNumber()).isEqualTo(roomNumber1);
        assertThat(result.getId()).isNull();
    }

    @Test
    void toResponseDto_shouldMapAllFields_whenFacultyIsValid() {
        RoomResponseDto result = roomMapper.toResponseDto(room1);
        assertThat(result.id()).isEqualTo(room1.getId());
        assertThat(result.roomNumber()).isEqualTo(roomNumber1);
    }

    @Test
    void toResponseDtoList_shouldMapAllElements_whenListIsNotEmpty() {
        List<Room> rooms = List.of(room1, room2);
        List<RoomResponseDto> results = roomMapper.toResponseDtoList(rooms);

        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(0)).isEqualTo(roomResponseDto1);
        assertThat(results.get(1)).isEqualTo(roomResponseDto2);
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenListIsEmpty() {
        List<Room> rooms = List.of();
        List<RoomResponseDto> results = roomMapper.toResponseDtoList(rooms);

        assertThat(results).isEmpty();
    }

    @Test
    void updateEntity_shouldUpdateAllFields_whenRequestDtoIsValid() {
        roomMapper.updateEntityFromDto(roomRequestDto, room2);
        assertThat(room2.getRoomNumber()).isEqualTo(roomNumber1);
    }

    @Test
    void updateEntity_shouldNotChangeId_whenUpdating() {
        UUID id = room2.getId();
        roomMapper.updateEntityFromDto(roomRequestDto, room2);
        assertThat(room2.getId()).isEqualTo(id);
    }

    @Test
    void toResponseDto_shouldSetCategoryIdToNull_whenMappingFromRoomEntity() {
        RoomResponseDto result = roomMapper.toResponseDto(room1);

        assertThat(result.categoryId()).isNull();
    }
}