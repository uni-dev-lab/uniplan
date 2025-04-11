package org.unilab.uniplan.room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyService;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final FacultyService facultyService;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository,
                       FacultyService facultyService,
                       RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.facultyService = facultyService;
        this.roomMapper = roomMapper;
    }

    @Transactional
    public RoomDto createRoom(RoomDto roomDto) {
        Faculty faculty = facultyService.getFaculty(roomDto.facultyId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                            "Faculty not found"));
        Room room = roomMapper.toEntity(roomDto);
        room.setRoomNumber(roomDto.roomNumber());
        room.setFaculty(faculty);
        room = roomRepository.save(room);
        return roomMapper.toDto(room);
    }

    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(roomMapper::toDto).toList();
    }

    public Optional<RoomDto> getRoomById(UUID id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(roomMapper::toDto);
    }

    @Transactional
    public Optional<RoomDto> updateRoom(UUID id, RoomDto roomDto) {
        Optional<Room> existingRoom = roomRepository.findById(id);
        if (existingRoom.isPresent()) {
            Faculty faculty = facultyService
                .getFaculty(roomDto.facultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));
            Room room = existingRoom.get();
            room.setRoomNumber(roomDto.roomNumber());
            room.setFaculty(faculty);
            room = roomRepository.save(room);
            return Optional.of(roomMapper.toDto(room));
        }
        return Optional.empty();
    }

    @Transactional
    public boolean deleteRoom(UUID id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Room getRoomEntity(UUID id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
            "Room not found"));
    }
}
