package org.unilab.uniplan.room;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.unilab.uniplan.room.dto.RoomRequestDto;
import org.unilab.uniplan.room.dto.RoomResponseDto;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Manage classrooms and lecture halls, including room numbers and assigned faculties")
public class RoomController {

    private final RoomWebFacade roomWebFacade;

    @PostMapping
    public ResponseEntity<Void> createRoom(@Valid @NotNull @RequestBody final RoomRequestDto roomRequestDto) {
        roomWebFacade.createRoom(roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        return ResponseEntity.ok(roomWebFacade.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getRoomById(@PathVariable final UUID id) {
        return ResponseEntity.ok(roomWebFacade.getRoomById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable final UUID id,
                                                      @Valid @NotNull @RequestBody final RoomRequestDto roomRequestDto) {
        roomWebFacade.updateRoom(id, roomRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable final UUID id) {
        roomWebFacade.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }
}
