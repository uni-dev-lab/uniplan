package org.unilab.uniplan.building.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record BuildingRequestDto (
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be at most 50 characters")
    String name,

    @NotBlank(message="Address is required")
    @Size(max = 100, message = "Address must be at most 100 characters")
    String address,

    @NotNull(message = "University is required")
    UUID universityId
){}
