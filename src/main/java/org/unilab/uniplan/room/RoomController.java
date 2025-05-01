package org.unilab.uniplan.room;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.room.dto.RoomDto;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private static final String ROOM_NOT_FOUND = "Room with ID {0} not found.";

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@Valid @NotNull @RequestBody final RoomRequestDto roomRequestDto) {
        final RoomDto roomDto = roomService.createRoom(roomMapper.toInternalDto(roomRequestDto));

        return new ResponseEntity<>(roomMapper.toResponseDto(roomDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<RoomResponseDto> getAllRooms() {
        return roomMapper.toResponseDtoList(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable final UUID id) {
        final RoomDto roomDto = roomService.getRoomById(id)
                                           .orElseThrow(() -> new ResponseStatusException(
                                               HttpStatus.NOT_FOUND,
                                               MessageFormat.format(ROOM_NOT_FOUND, id)
                                           ));
        return ResponseEntity.ok(roomMapper.toResponseDto(roomDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable final UUID id,
                                                      @Valid @NotNull @RequestBody final RoomRequestDto roomRequestDto) {
        final RoomDto internalDto = roomMapper.toInternalDto(roomRequestDto);
        return roomService.updateRoom(id, internalDto)
                          .map(roomMapper::toResponseDto)
                          .map(ResponseEntity::ok)
                          .orElseThrow(() -> new ResponseStatusException(
                              HttpStatus.NOT_FOUND,
                              MessageFormat.format(ROOM_NOT_FOUND, id)
                          ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final UUID id) {
        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }
}
