package org.unilab.uniplan.roomcategory;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@RestController
@RequestMapping("/room-categories")
@RequiredArgsConstructor
public class RoomCategoryController {

    public static final String ROOM_CATEGORY_NOT_FOUND = "Room Category with Room ID {0} and Category ID {1} not found.";

    private final RoomCategoryService roomCategoryService;
    private final RoomCategoryMapper roomCategoryMapper;

    @PostMapping
    public ResponseEntity<RoomCategoryResponseDto> createRoomCategory(
        @Valid @NotNull @RequestBody final RoomCategoryRequestDto roomCategoryRequestDto) {

        RoomCategoryDto roomCategoryDto = roomCategoryService.createRoomCategory(
            roomCategoryMapper.toInternalDto(roomCategoryRequestDto));

        return new ResponseEntity<>(roomCategoryMapper.toResponseDto(roomCategoryDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public List<RoomCategoryResponseDto> getAllRoomCategories() {
        return roomCategoryMapper.toResponseDtoList(roomCategoryService.getAllRoomCategories());
    }

    @GetMapping("/{roomId}/{categoryId}")
    public ResponseEntity<RoomCategoryResponseDto> getRoomCategoryById(@PathVariable final UUID roomId,
                                                                       @PathVariable final UUID categoryId) {
        RoomCategoryDto roomCategoryDto = roomCategoryService.getRoomCategoryById(roomId,
                                                                                  categoryId)
                                                             .orElseThrow(() -> new ResponseStatusException(
                                                                 HttpStatus.NOT_FOUND,
                                                                 MessageFormat.format(
                                                                     ROOM_CATEGORY_NOT_FOUND,
                                                                     roomId,
                                                                     categoryId)));

        return ResponseEntity.ok(roomCategoryMapper.toResponseDto(roomCategoryDto));
    }


    @DeleteMapping("/{roomId}/{categoryId}")
    public ResponseEntity<Void> deleteRoomCategory(@PathVariable final UUID roomId,
                                                   @PathVariable final UUID categoryId) {
        roomCategoryService.deleteRoomCategory(roomId, categoryId);
        return ResponseEntity.noContent().build();
    }
}