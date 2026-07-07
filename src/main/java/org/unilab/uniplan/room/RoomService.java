package org.unilab.uniplan.room;

import static org.unilab.uniplan.utils.ErrorConstants.ROOM_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.common.model.BaseService;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.room.dto.RoomRequestDto;

@Service
@RequiredArgsConstructor
public class RoomService implements BaseService<Room> {

    private final RoomRepository roomRepository;

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
}
