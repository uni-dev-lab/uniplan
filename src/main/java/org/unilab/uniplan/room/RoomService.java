package org.unilab.uniplan.room;

import static org.unilab.uniplan.utils.ErrorConstants.ROOM_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.room.dto.RoomDto;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomDto createRoom(final RoomDto roomDto) {
        final Room room = roomMapper.toEntity(roomDto);

        return saveEntityAndConvertToDto(room);
    }

    public List<RoomDto> getAllRooms() {
        return roomMapper.toDtoList(roomRepository.findAll());
    }

    public RoomDto getRoomById(final UUID id) {
        return roomRepository.findById(id)
                             .map(roomMapper::toDto)
                             .orElseThrow(() -> new ResourceNotFoundException(ROOM_NOT_FOUND.getMessage(
                                 String.valueOf(id))));
    }

    @Transactional
    public RoomDto updateRoom(final UUID id, final RoomDto roomDto) {
        return roomRepository.findById(id)
                             .map(existingRoom -> updateEntityAndConvertToDto(
                                 roomDto,
                                 existingRoom)).orElseThrow(() -> new ResourceNotFoundException(
                ROOM_NOT_FOUND.getMessage(String.valueOf(id))));
    }

    @Transactional
    public void deleteRoom(final UUID id) {
        final Room room = roomRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                            ROOM_NOT_FOUND.getMessage(String.valueOf(id))));
        roomRepository.delete(room);
    }

    private RoomDto updateEntityAndConvertToDto(final RoomDto dto,
                                                final Room entity) {
        roomMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private RoomDto saveEntityAndConvertToDto(final Room entity) {
        final Room savedEntity = roomRepository.save(entity);
        return roomMapper.toDto(savedEntity);
    }
}
