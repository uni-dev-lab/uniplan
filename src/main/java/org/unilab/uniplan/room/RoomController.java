package org.unilab.uniplan.room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private static final String ROOM_NOT_FOUND = "The room is not found.";

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public RoomDto createRoom(@RequestBody RoomDto roomDto) {
        return roomService.createRoom(roomDto);
    }

    @GetMapping
    public List<RoomDto> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public RoomDto getRoomById(@PathVariable UUID id) {
        Optional<RoomDto> room = roomService.getRoomById(id);
        return room.orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND));
    }

    @PutMapping("/{id}")
    public RoomDto updateRoom(@PathVariable UUID id, @RequestBody RoomDto roomDto) {
        return roomService.updateRoom(id, roomDto)
                          .orElseThrow(() -> new IllegalArgumentException(ROOM_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable UUID id) {
        boolean isDeleted = roomService.deleteRoom(id);
        if (!isDeleted) {
            throw new IllegalArgumentException(ROOM_NOT_FOUND);
        }
    }
}
