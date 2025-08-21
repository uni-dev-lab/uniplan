package org.unilab.uniplan.roomcategory;

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
import org.unilab.uniplan.roomcategory.dto.RoomCategoryDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryRequestDto;
import org.unilab.uniplan.roomcategory.dto.RoomCategoryResponseDto;

@RestController
@RequestMapping("/roomCategories")
@RequiredArgsConstructor
public class RoomCategoryController {

    private final RoomCategoryService roomCategoryService;
    private final RoomCategoryMapper roomCategoryMapper;

    @PostMapping("/addRoomCategory")
    public ResponseEntity<RoomCategoryResponseDto> createRoomCategory(
        @Valid @NotNull @RequestBody final RoomCategoryRequestDto roomCategoryRequestDto) {

        RoomCategoryDto roomCategoryDto = roomCategoryService.createRoomCategory(
            roomCategoryMapper.toInternalDto(roomCategoryRequestDto));

        return new ResponseEntity<>(roomCategoryMapper.toResponseDto(roomCategoryDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping("/getAllRoomCategories")
    public List<RoomCategoryResponseDto> getAllRoomCategories() {
        return roomCategoryMapper.toResponseDtoList(roomCategoryService.getAllRoomCategories());
    }

    @GetMapping("/getRoomCategoryById")
    public ResponseEntity<RoomCategoryResponseDto> getRoomCategoryById(@RequestParam final UUID roomId,
                                                                       @RequestParam final UUID categoryId) {
        RoomCategoryDto roomCategoryDto = roomCategoryService.getRoomCategoryById(roomId,
                                                                                  categoryId);

        return ResponseEntity.ok(roomCategoryMapper.toResponseDto(roomCategoryDto));
    }


    @DeleteMapping("/deleteRoomCategory")
    public ResponseEntity<Void> deleteRoomCategory(@RequestParam final UUID roomId,
                                                   @RequestParam final UUID categoryId) {
        roomCategoryService.deleteRoomCategory(roomId, categoryId);
        return ResponseEntity.noContent().build();
    }
}