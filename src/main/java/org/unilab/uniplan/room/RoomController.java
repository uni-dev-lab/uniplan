package org.unilab.uniplan.room;

import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.unilab.uniplan.room.dto.RoomDto;
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

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
        final RoomDto roomDto = roomService.getRoomById(id);

        return ok(roomMapper.toResponseDto(roomDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable final UUID id,
                                                      @Valid @NotNull @RequestBody final RoomRequestDto roomRequestDto) {
        final RoomDto internalDto = roomMapper.toInternalDto(roomRequestDto);

        return ok(roomMapper.toResponseDto(roomService.updateRoom(id, internalDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final UUID id) {
        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }
}
