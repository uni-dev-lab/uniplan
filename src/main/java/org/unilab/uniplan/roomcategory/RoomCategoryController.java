package org.unilab.uniplan.roomcategory;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@RestController
@RequestMapping("/room-categories")
@RequiredArgsConstructor
@Tag(name = "Room-Category Assignments", description = "Manage the association of rooms with categories")
public class RoomCategoryController {

    private final RoomCategoryWebFacade roomCategoryWebFacade;

    @PostMapping("/add")
    public ResponseEntity<Void> createRoomCategory(
        @Valid @NotNull @RequestBody final RoomCategoryRequestDto roomCategoryRequestDto) {

        roomCategoryWebFacade.createRoomCategory(roomCategoryRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RoomCategoryResponseDto>> getAllRoomCategories() {
        return ResponseEntity.ok(roomCategoryWebFacade.getAllRoomCategories());
    }

    @GetMapping("/getById")
    public ResponseEntity<RoomCategoryResponseDto> getRoomCategoryById(@RequestParam final UUID roomId,
                                                                       @RequestParam final UUID categoryId) {
        return ResponseEntity.ok(roomCategoryWebFacade.getRoomCategoryById(roomId, categoryId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRoomCategory(@RequestParam final UUID roomId,
                                                   @RequestParam final UUID categoryId) {
        roomCategoryWebFacade.deleteRoomCategory(roomId, categoryId);
        return ResponseEntity.noContent().build();
    }
}