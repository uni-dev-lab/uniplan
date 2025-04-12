package org.unilab.uniplan.room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.faculty.FacultyService;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final FacultyService facultyService;
    private final RoomMapper roomMapper;

    @Transactional
    public Optional<RoomDto> createRoom(final RoomDto roomDto) {
        final Room room = roomMapper.toEntity(roomDto);

        if (room.getRoomNumber() == null || room.getFaculty() == null) {
            return Optional.empty();
        }

        final Room savedRoom = roomRepository.save(room);
        return Optional.of(roomMapper.toDto(savedRoom));
    }

    public List<RoomDto> getAllRooms() {
        final List<Room> rooms = roomRepository.findAll();
        return roomMapper.toDtoList(rooms);
    }

    public Optional<RoomDto> getRoomById(final UUID id) {
        final Optional<Room> room = roomRepository.findById(id);
        return room.map(roomMapper::toDto);
    }

    @Transactional
    public Optional<RoomDto> updateRoom(final UUID id, final RoomDto roomDto) {
        final Optional<Room> existingRoom = roomRepository.findById(id);

        if (existingRoom.isPresent()) {
            final Room room = existingRoom.get();
            roomMapper.updateEntityFromDto(roomDto, room);

            final Room savedRoom = roomRepository.save(room);
            return Optional.of(roomMapper.toDto(savedRoom));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<RoomDto> deleteRoom(final UUID id) {
        final Optional<Room> roomOpt = roomRepository.findById(id);

        if (roomOpt.isPresent()) {
            final Room room = roomOpt.get();
            roomRepository.delete(room);
            return Optional.of(roomMapper.toDto(room));
        }

        return Optional.empty();
    }
}
