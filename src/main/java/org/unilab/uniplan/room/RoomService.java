package org.unilab.uniplan.room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@Service
@RequiredArgsConstructor
public class RoomService implements BaseService<Room> {

    private final RoomRepository roomRepository;
    private final FacultyRepository facultyRepository;

    public void setFaculty(Room room, UUID facultyId) {
        if (facultyId == null) {
            room.setFaculty(null);
            return;
        }

        //TODO: Change to FacultyNotFoundException after pr is approved
        Faculty faculty = facultyRepository.findById(facultyId)
                                           .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));

        room.setFaculty(faculty);
    }

    @Override
    public void save(final Room room) {
        roomRepository.save(room);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> getById(final UUID id) {
        return roomRepository.findById(id);
    }

    @Override
    public void delete(final Room room) {
        roomRepository.delete(room);
    }

    public List<RoomResponseDto> getAllRoomResponses() {
        return roomRepository.findAllRoomResponses();
    }

    public Optional<RoomResponseDto> getRoomResponseById(final UUID id) {
        return roomRepository.findRoomResponseById(id);
    }
}
