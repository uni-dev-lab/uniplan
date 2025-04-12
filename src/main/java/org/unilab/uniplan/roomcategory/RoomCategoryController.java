package org.unilab.uniplan.roomcategory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping("/roomcategories")
@RequiredArgsConstructor
public class RoomCategoryController {

    private final RoomCategoryService roomCategoryService;

    @PostMapping
    public ResponseEntity<RoomCategoryDto> createRoomCategory(@Valid @RequestBody final RoomCategoryDto roomCategoryDto) {
        final Optional<RoomCategoryDto> createdRoomCategory = roomCategoryService.createRoomCategory(
            roomCategoryDto);
        return createdRoomCategory
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity<List<RoomCategoryDto>> getAllRoomCategories() {
        final List<RoomCategoryDto> roomCategories = roomCategoryService.getAllRoomCategories();
        return roomCategories.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(roomCategories, HttpStatus.OK);
    }

    @GetMapping("/{roomId}/{categoryId}")
    public ResponseEntity<RoomCategoryDto> getRoomCategoryById(@PathVariable final UUID roomId,
                                                               @PathVariable final UUID categoryId) {
        final Optional<RoomCategoryDto> roomCategory = roomCategoryService.getRoomCategoryById(
            roomId,
            categoryId);
        return roomCategory
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/{roomId}/{categoryId}")
    public ResponseEntity<RoomCategoryDto> deleteRoomCategory(@PathVariable final UUID roomId,
                                                              @PathVariable final UUID categoryId) {
        final Optional<RoomCategoryDto> deletedRoomCategory = roomCategoryService.deleteRoomCategory(
            roomId,
            categoryId);
        return deletedRoomCategory
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}