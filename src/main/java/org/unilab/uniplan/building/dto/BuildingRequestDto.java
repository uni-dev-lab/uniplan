package org.unilab.uniplan.building.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class BuildingRequestDto {
    UUID id;
    @NotBlank(message="Name is required")
    @Size(max = 100)
    String name;
    @NotBlank(message="Address is required")
    @Size(max = 100)
    String address;
    @NotBlank(message = "University is required")
    @Size(max = 100)
    String university;
}
