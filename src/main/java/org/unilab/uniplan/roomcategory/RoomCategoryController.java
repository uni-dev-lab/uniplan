package org.unilab.uniplan.roomcategory;

import java.util.List;
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
@RequestMapping("/roomcategories")
public class RoomCategoryController {

    private final RoomCategoryService roomCategoryService;

    @Autowired
    public RoomCategoryController(RoomCategoryService roomCategoryService) {
        this.roomCategoryService = roomCategoryService;
    }

    @PostMapping
    public RoomCategoryDto createRoomCategory(@RequestBody RoomCategoryDto roomCategoryDto) {
        return roomCategoryService.createRoomCategory(roomCategoryDto);
    }

    @GetMapping
    public List<RoomCategoryDto> getAllRoomCategories() {
        return roomCategoryService.getAllRoomCategories();
    }

    @GetMapping("/{roomId}/{categoryId}")
    public RoomCategoryDto getRoomCategoryById(@PathVariable UUID roomId,
                                               @PathVariable UUID categoryId) {
        return roomCategoryService.getRoomCategoryById(roomId, categoryId);
    }

    @PutMapping("/{roomId}/{categoryId}")
    public RoomCategoryDto updateRoomCategory(@PathVariable UUID roomId,
                                              @PathVariable UUID categoryId) {

        RoomCategoryDto roomCategoryDto = new RoomCategoryDto(roomId, categoryId);

        return roomCategoryService.updateRoomCategory(roomCategoryDto);
    }

    @DeleteMapping("/{roomId}/{categoryId}")
    public void deleteRoomCategory(@PathVariable UUID roomId, @PathVariable UUID categoryId) {
        roomCategoryService.deleteRoomCategory(roomId, categoryId);
    }
}
