package org.unilab.uniplan.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;
import java.util.List;
import java.util.UUID;
import static org.unilab.uniplan.utils.ErrorConstants.ROOM_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomWebFacade {

    private final RoomMapper roomMapper;
    private final RoomService roomService;

    private Room getRoomOrThrow(final UUID id) {
        return roomService.getById(id)
                             .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND.getMessage(
                                 String.valueOf(id))));
    }

    @Transactional
    public void createRoom(RoomRequestDto roomRequestDto) {
        Room room = roomMapper.toEntity(roomRequestDto);

        roomService.setFaculty(room, roomRequestDto.facultyId());

        roomService.save(room);
        log.info("created room {} with ID: {}",
                room.getRoomNumber(),
                room.getId());
    }

    @Transactional(readOnly = true)
    public List<RoomResponseDto> getAllRooms() {
        return roomService.getAllRoomResponses();
    }

    @Transactional
    public void deleteRoom(final UUID id) {
        final Room room = getRoomOrThrow(id);
        roomService.delete(room);
        log.info("deleted room with id {}", id);
    }

    @Transactional(readOnly = true)
    public RoomResponseDto getRoomById(final UUID id) {
        return roomService.getRoomResponseById(id)
                          .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND.getMessage(
                              String.valueOf(id))));
    }

    @Transactional
    public void updateRoom(final UUID id, final RoomRequestDto roomRequestDto) {
        final Room room = getRoomOrThrow(id);
        roomMapper.updateEntityFromDto(roomRequestDto, room);
        roomService.setFaculty(room, roomRequestDto.facultyId());
        roomService.save(room);
        log.info("updated room with id {}", id);
    }
}
