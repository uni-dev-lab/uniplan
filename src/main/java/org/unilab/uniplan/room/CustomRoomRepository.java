package org.unilab.uniplan.room;

import org.unilab.uniplan.room.dto.RoomResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomRoomRepository {

    List<RoomResponseDto> findAllRoomResponses();

    Optional<RoomResponseDto> findRoomResponseById(UUID id);
}
