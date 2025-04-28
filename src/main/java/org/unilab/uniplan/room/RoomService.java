package org.unilab.uniplan.room;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.room.dto.RoomDto;

@Service
@RequiredArgsConstructor
public class RoomService {

    public static final String ROOM_NOT_FOUND = "Room with ID {0} not found.";

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomDto createRoom(final RoomDto roomDto) {
        final Room room = roomMapper.toEntity(roomDto);

        return roomMapper.toDto(roomRepository.save(room));
    }

    public List<RoomDto> getAllRooms() {
        return roomMapper.toDtoList(roomRepository.findAll());
    }

    public Optional<RoomDto> getRoomById(final UUID id) {
        return roomRepository.findById(id)
                             .map(roomMapper::toDto);
    }

    @Transactional
    public Optional<RoomDto> updateRoom(final UUID id, final RoomDto roomDto) {
        return roomRepository.findById(id)
                             .map(existingRoom -> {
                                 roomMapper.updateEntityFromDto(roomDto, existingRoom);

                                 return roomMapper.toDto(roomRepository.save(existingRoom));
                             });
    }

    @Transactional
    public void deleteRoom(final UUID id) {
        final Room room = roomRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException(
                                            MessageFormat.format(ROOM_NOT_FOUND, id)));
        roomRepository.delete(room);
    }
}
