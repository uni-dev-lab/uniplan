package org.unilab.uniplan.building;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.building.dto.BuildingResponseDto;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
@Tag(name = "Buildings", description = "Manage university buildings, including name, address, and associated university")
public class BuildingController {
    private final BuildingWebFacade buildingWebFacade;

    @PostMapping
    public ResponseEntity<Void> createBuilding(@Valid @NotNull @RequestBody final BuildingRequestDto buildingRequestDto) {
        buildingWebFacade.createBuilding(buildingRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<BuildingResponseDto>> getAllBuildings() {
        return ResponseEntity.ok(buildingWebFacade.getAllBuildings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponseDto> getBuildingById(@PathVariable final UUID id) {
        return ResponseEntity.ok(buildingWebFacade.getBuildingById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBuilding(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final BuildingRequestDto buildingRequestDto) {
        buildingWebFacade.updateBuilding(id, buildingRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable final UUID id) {
        buildingWebFacade.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}
